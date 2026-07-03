-- ============================================================
-- Sakila Rental - Performance Indexes
-- Run after schema.sql and seed.sql
-- ============================================================

CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- ============================================================
-- STANDARD INDEXES (from Sakila schema)
-- ============================================================

CREATE INDEX idx_actor_last_name ON actor USING btree (last_name);
CREATE INDEX idx_fk_address_id ON customer USING btree (address_id);
CREATE INDEX idx_fk_city_id ON address USING btree (city_id);
CREATE INDEX idx_fk_country_id ON city USING btree (country_id);
CREATE INDEX idx_fk_customer_id ON payment USING btree (customer_id);
CREATE INDEX idx_fk_film_id ON film_actor USING btree (film_id);
CREATE INDEX idx_fk_inventory_id ON rental USING btree (inventory_id);
CREATE INDEX idx_fk_language_id ON film USING btree (language_id);
CREATE INDEX idx_fk_original_language_id ON film USING btree (original_language_id);
CREATE INDEX idx_fk_staff_id ON payment USING btree (staff_id);
CREATE INDEX idx_fk_store_id ON customer USING btree (store_id);
CREATE INDEX idx_last_name ON customer USING btree (last_name);
CREATE INDEX idx_store_id_film_id ON inventory USING btree (store_id, film_id);
CREATE INDEX idx_title ON film USING btree (title);

-- ============================================================
-- ADDITIONAL PERFORMANCE INDEXES
-- ============================================================

-- Accelerate title searches (ILIKE / fuzzy)
CREATE INDEX idx_film_title_trgm ON film USING gin (title gin_trgm_ops);

-- Accelerate full-text searches on film descriptions
CREATE INDEX idx_film_description_trgm ON film USING gin (description gin_trgm_ops);

-- Composite index for active rentals per customer (frequent query)
CREATE INDEX idx_rental_active ON rental (customer_id, return_date)
  WHERE return_date IS NULL;

-- Composite index for rental history queries
CREATE INDEX idx_rental_customer_date ON rental (customer_id, rental_date DESC);

-- Film inventory availability (frequent stock check)
CREATE INDEX idx_inventory_film ON inventory (film_id, store_id);

-- Payment history for customer balance
CREATE INDEX idx_payment_customer_date ON payment (customer_id, payment_date DESC);

-- Category name fast lookup
CREATE INDEX idx_category_name ON category USING btree (name);

-- Customer email lookups for auth
CREATE INDEX idx_customer_email ON customer USING btree (email);

-- Film release year for filtering
CREATE INDEX idx_film_release_year ON film USING btree (release_year);

-- ============================================================
-- UNIQUE CONSTRAINTS (additional)
-- ============================================================

CREATE UNIQUE INDEX idx_unq_rental_rental_date_inventory_id_customer_id
  ON rental USING btree (rental_date, inventory_id, customer_id);

CREATE UNIQUE INDEX idx_unq_manager_staff_id
  ON store USING btree (manager_staff_id);

-- ============================================================
-- MAINTENANCE
-- ============================================================

ANALYZE;
