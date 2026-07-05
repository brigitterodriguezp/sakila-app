-- ============================================================
-- Sakila Rental - Database Schema (PostgreSQL)
-- Based on jOOQ Sakila port + Pagila enhancements
-- Structure only (no data)
-- ============================================================

SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;

CREATE TYPE mpaa_rating AS ENUM ('G', 'PG', 'PG-13', 'R', 'NC-17');

CREATE DOMAIN year AS integer
  CONSTRAINT year_check CHECK (((VALUE >= 1901) AND (VALUE <= 2155)));

-- ============================================================
-- FUNCTIONS
-- ============================================================

CREATE FUNCTION _group_concat(text, text) RETURNS text
  LANGUAGE sql IMMUTABLE AS $_$
  SELECT CASE WHEN $2 IS NULL THEN $1 WHEN $1 IS NULL THEN $2 ELSE $1 || ', ' || $2 END;
$_$;

CREATE AGGREGATE group_concat(text) (SFUNC = _group_concat, STYPE = text);

CREATE FUNCTION last_updated() RETURNS trigger
  LANGUAGE plpgsql AS $$ BEGIN NEW.last_update = CURRENT_TIMESTAMP; RETURN NEW; END $$;

CREATE FUNCTION inventory_in_stock(p_inventory_id integer) RETURNS boolean
  LANGUAGE plpgsql AS $$
DECLARE v_rentals INTEGER; v_out INTEGER;
BEGIN
  SELECT count(*) INTO v_rentals FROM rental WHERE inventory_id = p_inventory_id;
  IF v_rentals = 0 THEN RETURN TRUE; END IF;
  SELECT COUNT(rental_id) INTO v_out FROM inventory LEFT JOIN rental USING(inventory_id)
    WHERE inventory.inventory_id = p_inventory_id AND rental.return_date IS NULL;
  RETURN v_out <= 0;
END $$;

CREATE FUNCTION film_in_stock(p_film_id integer, p_store_id integer, OUT p_film_count integer)
  RETURNS SETOF integer LANGUAGE sql AS $_$
  SELECT inventory_id FROM inventory
    WHERE film_id = $1 AND store_id = $2 AND inventory_in_stock(inventory_id);
$_$;

CREATE FUNCTION film_not_in_stock(p_film_id integer, p_store_id integer, OUT p_film_count integer)
  RETURNS SETOF integer LANGUAGE sql AS $_$
  SELECT inventory_id FROM inventory
    WHERE film_id = $1 AND store_id = $2 AND NOT inventory_in_stock(inventory_id);
$_$;

CREATE FUNCTION get_customer_balance(p_customer_id integer, p_effective_date timestamp)
  RETURNS numeric LANGUAGE plpgsql AS $$
DECLARE v_rentfees DECIMAL(5,2); v_overfees INTEGER; v_payments DECIMAL(5,2);
BEGIN
  SELECT COALESCE(SUM(f.rental_rate),0) INTO v_rentfees FROM film f, inventory i, rental r
    WHERE f.film_id = i.film_id AND i.inventory_id = r.inventory_id
    AND r.rental_date <= p_effective_date AND r.customer_id = p_customer_id;
  SELECT COALESCE(SUM(EXTRACT(EPOCH FROM (r.return_date - r.rental_date))/86400 - f.rental_duration),0)
    INTO v_overfees FROM rental r, inventory i, film f
    WHERE f.film_id = i.film_id AND i.inventory_id = r.inventory_id
    AND r.rental_date <= p_effective_date AND r.customer_id = p_customer_id;
  SELECT COALESCE(SUM(p.amount),0) INTO v_payments FROM payment p
    WHERE p.payment_date <= p_effective_date AND p.customer_id = p_customer_id;
  RETURN v_rentfees + GREATEST(v_overfees, 0) - v_payments;
END $$;

CREATE FUNCTION rewards_report(min_monthly_purchases integer, min_dollar_amount_purchased numeric)
  RETURNS SETOF customer LANGUAGE plpgsql SECURITY DEFINER AS $_$
DECLARE last_month_start DATE; last_month_end DATE; rr RECORD; tmpSQL TEXT;
BEGIN
  IF min_monthly_purchases = 0 THEN RAISE EXCEPTION 'min_monthly_purchases must be > 0'; END IF;
  IF min_dollar_amount_purchased = 0.00 THEN RAISE EXCEPTION 'min_dollar_amount must be > $0.00'; END IF;
  last_month_start := date_trunc('month', CURRENT_DATE - INTERVAL '3 months')::date;
  last_month_end := (date_trunc('month', last_month_start) + INTERVAL '1 month' - INTERVAL '1 day')::date;
  CREATE TEMPORARY TABLE tmpCustomer (customer_id INTEGER NOT NULL PRIMARY KEY);
  tmpSQL := 'INSERT INTO tmpCustomer (customer_id) SELECT p.customer_id FROM payment AS p
    WHERE DATE(p.payment_date) BETWEEN ' || quote_literal(last_month_start) || ' AND ' || quote_literal(last_month_end) || '
    GROUP BY customer_id HAVING SUM(p.amount) > ' || min_dollar_amount_purchased || ' AND COUNT(customer_id) > ' || min_monthly_purchases;
  EXECUTE tmpSQL;
  FOR rr IN EXECUTE 'SELECT c.* FROM tmpCustomer t INNER JOIN customer c ON t.customer_id = c.customer_id' LOOP
    RETURN NEXT rr;
  END LOOP;
  EXECUTE 'DROP TABLE tmpCustomer';
  RETURN;
END $_$;

-- ============================================================
-- TABLES
-- ============================================================

CREATE SEQUENCE actor_actor_id_seq INCREMENT BY 1 NO MAXVALUE NO MINVALUE CACHE 1;
CREATE TABLE actor (
  actor_id integer DEFAULT nextval('actor_actor_id_seq'::regclass) NOT NULL,
  first_name character varying(45) NOT NULL,
  last_name character varying(45) NOT NULL,
  last_update timestamp without time zone DEFAULT now() NOT NULL,
  PRIMARY KEY (actor_id)
);

CREATE SEQUENCE category_category_id_seq INCREMENT BY 1 NO MAXVALUE NO MINVALUE CACHE 1;
CREATE TABLE category (
  category_id integer DEFAULT nextval('category_category_id_seq'::regclass) NOT NULL,
  name character varying(25) NOT NULL,
  last_update timestamp without time zone DEFAULT now() NOT NULL,
  PRIMARY KEY (category_id)
);

CREATE SEQUENCE film_film_id_seq INCREMENT BY 1 NO MAXVALUE NO MINVALUE CACHE 1;
CREATE TABLE film (
  film_id integer DEFAULT nextval('film_film_id_seq'::regclass) NOT NULL,
  title character varying(255) NOT NULL,
  description text,
  release_year year,
  language_id integer NOT NULL,
  original_language_id integer,
  rental_duration smallint DEFAULT 3 NOT NULL,
  rental_rate numeric(4,2) DEFAULT 4.99 NOT NULL,
  length smallint,
  replacement_cost numeric(5,2) DEFAULT 19.99 NOT NULL,
  rating mpaa_rating DEFAULT 'G'::mpaa_rating,
  last_update timestamp without time zone DEFAULT now() NOT NULL,
  special_features text[],
  fulltext tsvector NOT NULL,
  PRIMARY KEY (film_id)
);

CREATE TABLE film_actor (
  actor_id integer NOT NULL,
  film_id integer NOT NULL,
  last_update timestamp without time zone DEFAULT now() NOT NULL,
  PRIMARY KEY (actor_id, film_id)
);

CREATE TABLE film_category (
  film_id integer NOT NULL,
  category_id integer NOT NULL,
  last_update timestamp without time zone DEFAULT now() NOT NULL,
  PRIMARY KEY (film_id, category_id)
);

CREATE SEQUENCE address_address_id_seq INCREMENT BY 1 NO MAXVALUE NO MINVALUE CACHE 1;
CREATE TABLE address (
  address_id integer DEFAULT nextval('address_address_id_seq'::regclass) NOT NULL,
  address character varying(50) NOT NULL,
  address2 character varying(50),
  district character varying(20) NOT NULL,
  city_id integer NOT NULL,
  postal_code character varying(10),
  phone character varying(20) NOT NULL,
  last_update timestamp without time zone DEFAULT now() NOT NULL,
  PRIMARY KEY (address_id)
);

CREATE SEQUENCE city_city_id_seq INCREMENT BY 1 NO MAXVALUE NO MINVALUE CACHE 1;
CREATE TABLE city (
  city_id integer DEFAULT nextval('city_city_id_seq'::regclass) NOT NULL,
  city character varying(50) NOT NULL,
  country_id integer NOT NULL,
  last_update timestamp without time zone DEFAULT now() NOT NULL,
  PRIMARY KEY (city_id)
);

CREATE SEQUENCE country_country_id_seq INCREMENT BY 1 NO MAXVALUE NO MINVALUE CACHE 1;
CREATE TABLE country (
  country_id integer DEFAULT nextval('country_country_id_seq'::regclass) NOT NULL,
  country character varying(50) NOT NULL,
  last_update timestamp without time zone DEFAULT now() NOT NULL,
  PRIMARY KEY (country_id)
);

CREATE SEQUENCE customer_customer_id_seq INCREMENT BY 1 NO MAXVALUE NO MINVALUE CACHE 1;
CREATE TABLE customer (
  customer_id integer DEFAULT nextval('customer_customer_id_seq'::regclass) NOT NULL,
  store_id integer NOT NULL,
  first_name character varying(45) NOT NULL,
  last_name character varying(45) NOT NULL,
  email character varying(50),
  address_id integer NOT NULL,
  activebool boolean DEFAULT true NOT NULL,
  create_date date DEFAULT ('now'::text)::date NOT NULL,
  last_update timestamp without time zone DEFAULT now(),
  active integer,
  PRIMARY KEY (customer_id)
);

CREATE SEQUENCE inventory_inventory_id_seq INCREMENT BY 1 NO MAXVALUE NO MINVALUE CACHE 1;
CREATE TABLE inventory (
  inventory_id integer DEFAULT nextval('inventory_inventory_id_seq'::regclass) NOT NULL,
  film_id integer NOT NULL,
  store_id integer NOT NULL,
  last_update timestamp without time zone DEFAULT now() NOT NULL,
  PRIMARY KEY (inventory_id)
);

CREATE SEQUENCE language_language_id_seq INCREMENT BY 1 NO MAXVALUE NO MINVALUE CACHE 1;
CREATE TABLE language (
  language_id integer DEFAULT nextval('language_language_id_seq'::regclass) NOT NULL,
  name character(20) NOT NULL,
  last_update timestamp without time zone DEFAULT now() NOT NULL,
  PRIMARY KEY (language_id)
);

CREATE SEQUENCE payment_payment_id_seq INCREMENT BY 1 NO MAXVALUE NO MINVALUE CACHE 1;
CREATE TABLE payment (
  payment_id integer DEFAULT nextval('payment_payment_id_seq'::regclass) NOT NULL,
  customer_id integer NOT NULL,
  staff_id integer NOT NULL,
  rental_id integer NOT NULL,
  amount numeric(5,2) NOT NULL,
  payment_date timestamp without time zone NOT NULL,
  PRIMARY KEY (payment_id)
);

CREATE SEQUENCE rental_rental_id_seq INCREMENT BY 1 NO MAXVALUE NO MINVALUE CACHE 1;
CREATE TABLE rental (
  rental_id integer DEFAULT nextval('rental_rental_id_seq'::regclass) NOT NULL,
  rental_date timestamp without time zone NOT NULL,
  inventory_id integer NOT NULL,
  customer_id integer NOT NULL,
  return_date timestamp without time zone,
  staff_id integer NOT NULL,
  last_update timestamp without time zone DEFAULT now() NOT NULL,
  PRIMARY KEY (rental_id)
);

CREATE SEQUENCE staff_staff_id_seq INCREMENT BY 1 NO MAXVALUE NO MINVALUE CACHE 1;
CREATE TABLE staff (
  staff_id integer DEFAULT nextval('staff_staff_id_seq'::regclass) NOT NULL,
  first_name character varying(45) NOT NULL,
  last_name character varying(45) NOT NULL,
  address_id integer NOT NULL,
  email character varying(50),
  store_id integer NOT NULL,
  active boolean DEFAULT true NOT NULL,
  username character varying(16) NOT NULL,
  password character varying(40),
  last_update timestamp without time zone DEFAULT now() NOT NULL,
  picture bytea,
  PRIMARY KEY (staff_id)
);

CREATE SEQUENCE store_store_id_seq INCREMENT BY 1 NO MAXVALUE NO MINVALUE CACHE 1;
CREATE TABLE store (
  store_id integer DEFAULT nextval('store_store_id_seq'::regclass) NOT NULL,
  manager_staff_id integer NOT NULL,
  address_id integer NOT NULL,
  last_update timestamp without time zone DEFAULT now() NOT NULL,
  PRIMARY KEY (store_id)
);

CREATE TABLE users (
  id bigint GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  username character varying(50) NOT NULL,
  email character varying(100) NOT NULL,
  password character varying(255) NOT NULL,
  role character varying(20) DEFAULT 'USER' NOT NULL,
  active boolean DEFAULT true NOT NULL,
  created_at timestamp without time zone DEFAULT now() NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT users_username_key UNIQUE (username),
  CONSTRAINT users_email_key UNIQUE (email)
);

-- ============================================================
-- FOREIGN KEYS
-- ============================================================

ALTER TABLE address ADD CONSTRAINT address_city_id_fkey
  FOREIGN KEY (city_id) REFERENCES city(city_id) ON UPDATE CASCADE ON DELETE RESTRICT;
ALTER TABLE city ADD CONSTRAINT city_country_id_fkey
  FOREIGN KEY (country_id) REFERENCES country(country_id) ON UPDATE CASCADE ON DELETE RESTRICT;
ALTER TABLE customer ADD CONSTRAINT customer_address_id_fkey
  FOREIGN KEY (address_id) REFERENCES address(address_id) ON UPDATE CASCADE ON DELETE RESTRICT;
ALTER TABLE customer ADD CONSTRAINT customer_store_id_fkey
  FOREIGN KEY (store_id) REFERENCES store(store_id) ON UPDATE CASCADE ON DELETE RESTRICT;
ALTER TABLE film ADD CONSTRAINT film_language_id_fkey
  FOREIGN KEY (language_id) REFERENCES language(language_id) ON UPDATE CASCADE ON DELETE RESTRICT;
ALTER TABLE film ADD CONSTRAINT film_original_language_id_fkey
  FOREIGN KEY (original_language_id) REFERENCES language(language_id) ON UPDATE CASCADE ON DELETE RESTRICT;
ALTER TABLE film_actor ADD CONSTRAINT film_actor_actor_id_fkey
  FOREIGN KEY (actor_id) REFERENCES actor(actor_id) ON UPDATE CASCADE ON DELETE RESTRICT;
ALTER TABLE film_actor ADD CONSTRAINT film_actor_film_id_fkey
  FOREIGN KEY (film_id) REFERENCES film(film_id) ON UPDATE CASCADE ON DELETE RESTRICT;
ALTER TABLE film_category ADD CONSTRAINT film_category_category_id_fkey
  FOREIGN KEY (category_id) REFERENCES category(category_id) ON UPDATE CASCADE ON DELETE RESTRICT;
ALTER TABLE film_category ADD CONSTRAINT film_category_film_id_fkey
  FOREIGN KEY (film_id) REFERENCES film(film_id) ON UPDATE CASCADE ON DELETE RESTRICT;
ALTER TABLE inventory ADD CONSTRAINT inventory_film_id_fkey
  FOREIGN KEY (film_id) REFERENCES film(film_id) ON UPDATE CASCADE ON DELETE RESTRICT;
ALTER TABLE inventory ADD CONSTRAINT inventory_store_id_fkey
  FOREIGN KEY (store_id) REFERENCES store(store_id) ON UPDATE CASCADE ON DELETE RESTRICT;
ALTER TABLE payment ADD CONSTRAINT payment_customer_id_fkey
  FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON UPDATE CASCADE ON DELETE RESTRICT;
ALTER TABLE payment ADD CONSTRAINT payment_rental_id_fkey
  FOREIGN KEY (rental_id) REFERENCES rental(rental_id) ON UPDATE CASCADE ON DELETE SET NULL;
ALTER TABLE payment ADD CONSTRAINT payment_staff_id_fkey
  FOREIGN KEY (staff_id) REFERENCES staff(staff_id) ON UPDATE CASCADE ON DELETE RESTRICT;
ALTER TABLE rental ADD CONSTRAINT rental_customer_id_fkey
  FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON UPDATE CASCADE ON DELETE RESTRICT;
ALTER TABLE rental ADD CONSTRAINT rental_inventory_id_fkey
  FOREIGN KEY (inventory_id) REFERENCES inventory(inventory_id) ON UPDATE CASCADE ON DELETE RESTRICT;
ALTER TABLE rental ADD CONSTRAINT rental_staff_id_fkey
  FOREIGN KEY (staff_id) REFERENCES staff(staff_id) ON UPDATE CASCADE ON DELETE RESTRICT;
ALTER TABLE staff ADD CONSTRAINT staff_address_id_fkey
  FOREIGN KEY (address_id) REFERENCES address(address_id) ON UPDATE CASCADE ON DELETE RESTRICT;
ALTER TABLE staff ADD CONSTRAINT staff_store_id_fkey
  FOREIGN KEY (store_id) REFERENCES store(store_id);
ALTER TABLE store ADD CONSTRAINT store_address_id_fkey
  FOREIGN KEY (address_id) REFERENCES address(address_id) ON UPDATE CASCADE ON DELETE RESTRICT;
ALTER TABLE store ADD CONSTRAINT store_manager_staff_id_fkey
  FOREIGN KEY (manager_staff_id) REFERENCES staff(staff_id) ON UPDATE CASCADE ON DELETE RESTRICT;

-- ============================================================
-- VIEWS
-- ============================================================

CREATE VIEW actor_info AS
  SELECT a.actor_id, a.first_name, a.last_name,
    group_concat(DISTINCT (c.name || ': '::text) || (
      SELECT group_concat(f.title) FROM film f
        JOIN film_category fc_1 ON f.film_id = fc_1.film_id
        JOIN film_actor fa_1 ON f.film_id = fa_1.film_id
        WHERE fc_1.category_id = c.category_id AND fa_1.actor_id = a.actor_id
    )) AS film_info
  FROM actor a
    LEFT JOIN film_actor fa ON a.actor_id = fa.actor_id
    LEFT JOIN film_category fc ON fa.film_id = fc.film_id
    LEFT JOIN category c ON fc.category_id = c.category_id
  GROUP BY a.actor_id, a.first_name, a.last_name;

CREATE VIEW customer_list AS
  SELECT cu.customer_id AS id, (cu.first_name || ' ' || cu.last_name) AS name,
    a.address, a.postal_code AS "zip code", a.phone, ci.city, co.country,
    CASE WHEN cu.activebool THEN 'active' ELSE '' END AS notes, cu.store_id AS sid
  FROM customer cu
    JOIN address a ON cu.address_id = a.address_id
    JOIN city ci ON a.city_id = ci.city_id
    JOIN country co ON ci.country_id = co.country_id;

CREATE VIEW film_list AS
  SELECT f.film_id AS fid, f.title, f.description, c.name AS category,
    f.rental_rate AS price, f.length, f.rating,
    group_concat((a.first_name || ' ' || a.last_name)) AS actors
  FROM category c
    LEFT JOIN film_category fc ON c.category_id = fc.category_id
    LEFT JOIN film f ON fc.film_id = f.film_id
    JOIN film_actor fa ON f.film_id = fa.film_id
    JOIN actor a ON fa.actor_id = a.actor_id
  GROUP BY f.film_id, f.title, f.description, c.name, f.rental_rate, f.length, f.rating;

CREATE VIEW sales_by_film_category AS
  SELECT c.name AS category, SUM(p.amount) AS total_sales
  FROM payment p
    JOIN rental r ON p.rental_id = r.rental_id
    JOIN inventory i ON r.inventory_id = i.inventory_id
    JOIN film f ON i.film_id = f.film_id
    JOIN film_category fc ON f.film_id = fc.film_id
    JOIN category c ON fc.category_id = c.category_id
  GROUP BY c.name ORDER BY total_sales DESC;

CREATE VIEW staff_list AS
  SELECT s.staff_id AS id, (s.first_name || ' ' || s.last_name) AS name,
    a.address, a.postal_code AS "zip code", a.phone, ci.city, co.country, s.store_id AS sid
  FROM staff s
    JOIN address a ON s.address_id = a.address_id
    JOIN city ci ON a.city_id = ci.city_id
    JOIN country co ON ci.country_id = co.country_id;

CREATE VIEW sales_by_store AS
  SELECT (c.city || ',' || cy.country) AS store,
    (m.first_name || ' ' || m.last_name) AS manager, SUM(p.amount) AS total_sales
  FROM payment p
    JOIN rental r ON p.rental_id = r.rental_id
    JOIN inventory i ON r.inventory_id = i.inventory_id
    JOIN store s ON i.store_id = s.store_id
    JOIN address a ON s.address_id = a.address_id
    JOIN city c ON a.city_id = c.city_id
    JOIN country cy ON c.country_id = cy.country_id
    JOIN staff m ON s.manager_staff_id = m.staff_id
  GROUP BY cy.country, c.city, s.store_id, m.first_name, m.last_name
  ORDER BY cy.country, c.city;

CREATE VIEW nicer_but_slower_film_list AS
  SELECT f.film_id AS fid, f.title, f.description, c.name AS category,
    f.rental_rate AS price, f.length, f.rating,
    group_concat(INITCAP(a.first_name) || ' ' || INITCAP(a.last_name)) AS actors
  FROM category c
    LEFT JOIN film_category fc ON c.category_id = fc.category_id
    LEFT JOIN film f ON fc.film_id = f.film_id
    JOIN film_actor fa ON f.film_id = fa.film_id
    JOIN actor a ON fa.actor_id = a.actor_id
  GROUP BY f.film_id, f.title, f.description, c.name, f.rental_rate, f.length, f.rating;

-- ============================================================
-- TRIGGERS
-- ============================================================

CREATE TRIGGER film_fulltext_trigger
  BEFORE INSERT OR UPDATE ON film
  FOR EACH ROW EXECUTE FUNCTION tsvector_update_trigger('fulltext', 'pg_catalog.english', 'title', 'description');

CREATE TRIGGER last_updated_actor      BEFORE UPDATE ON actor    FOR EACH ROW EXECUTE FUNCTION last_updated();
CREATE TRIGGER last_updated_address    BEFORE UPDATE ON address  FOR EACH ROW EXECUTE FUNCTION last_updated();
CREATE TRIGGER last_updated_category   BEFORE UPDATE ON category FOR EACH ROW EXECUTE FUNCTION last_updated();
CREATE TRIGGER last_updated_city       BEFORE UPDATE ON city     FOR EACH ROW EXECUTE FUNCTION last_updated();
CREATE TRIGGER last_updated_country    BEFORE UPDATE ON country  FOR EACH ROW EXECUTE FUNCTION last_updated();
CREATE TRIGGER last_updated_customer   BEFORE UPDATE ON customer FOR EACH ROW EXECUTE FUNCTION last_updated();
CREATE TRIGGER last_updated_film       BEFORE UPDATE ON film     FOR EACH ROW EXECUTE FUNCTION last_updated();
CREATE TRIGGER last_updated_film_actor BEFORE UPDATE ON film_actor FOR EACH ROW EXECUTE FUNCTION last_updated();
CREATE TRIGGER last_updated_film_category BEFORE UPDATE ON film_category FOR EACH ROW EXECUTE FUNCTION last_updated();
CREATE TRIGGER last_updated_inventory  BEFORE UPDATE ON inventory FOR EACH ROW EXECUTE FUNCTION last_updated();
CREATE TRIGGER last_updated_language   BEFORE UPDATE ON language FOR EACH ROW EXECUTE FUNCTION last_updated();
CREATE TRIGGER last_updated_rental     BEFORE UPDATE ON rental   FOR EACH ROW EXECUTE FUNCTION last_updated();
CREATE TRIGGER last_updated_staff      BEFORE UPDATE ON staff    FOR EACH ROW EXECUTE FUNCTION last_updated();
CREATE TRIGGER last_updated_store      BEFORE UPDATE ON store    FOR EACH ROW EXECUTE FUNCTION last_updated();
