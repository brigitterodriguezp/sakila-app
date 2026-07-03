-- ============================================================
-- Sakila Rental - Seed Data
-- Core reference data for the rental store
-- ============================================================

-- ============================================================
-- LANGUAGE
-- ============================================================
INSERT INTO language (language_id, name, last_update) VALUES
  (1, 'English             ', '2022-02-15 09:34:33'),
  (2, 'Italian             ', '2022-02-15 09:34:33'),
  (3, 'Japanese            ', '2022-02-15 09:34:33'),
  (4, 'Mandarin            ', '2022-02-15 09:34:33'),
  (5, 'French              ', '2022-02-15 09:34:33'),
  (6, 'German              ', '2022-02-15 09:34:33');
SELECT setval('language_language_id_seq', 6);

-- ============================================================
-- CATEGORY
-- ============================================================
INSERT INTO category (category_id, name, last_update) VALUES
  (1,  'Action',        '2022-02-15 09:46:27'),
  (2,  'Animation',     '2022-02-15 09:46:27'),
  (3,  'Children',      '2022-02-15 09:46:27'),
  (4,  'Classics',      '2022-02-15 09:46:27'),
  (5,  'Comedy',        '2022-02-15 09:46:27'),
  (6,  'Documentary',   '2022-02-15 09:46:27'),
  (7,  'Drama',         '2022-02-15 09:46:27'),
  (8,  'Family',        '2022-02-15 09:46:27'),
  (9,  'Foreign',       '2022-02-15 09:46:27'),
  (10, 'Games',         '2022-02-15 09:46:27'),
  (11, 'Horror',        '2022-02-15 09:46:27'),
  (12, 'Music',         '2022-02-15 09:46:27'),
  (13, 'New',           '2022-02-15 09:46:27'),
  (14, 'Sci-Fi',        '2022-02-15 09:46:27'),
  (15, 'Sports',        '2022-02-15 09:46:27'),
  (16, 'Travel',        '2022-02-15 09:46:27');
SELECT setval('category_category_id_seq', 16);

-- ============================================================
-- COUNTRY
-- ============================================================
INSERT INTO country (country_id, country, last_update) VALUES
  (1,  'Afghanistan',                     '2022-02-15 09:44:00'),
  (2,  'Algeria',                         '2022-02-15 09:44:00'),
  (3,  'Argentina',                       '2022-02-15 09:44:00'),
  (4,  'Australia',                       '2022-02-15 09:44:00'),
  (5,  'Brazil',                          '2022-02-15 09:44:00'),
  (6,  'Canada',                          '2022-02-15 09:44:00'),
  (7,  'China',                           '2022-02-15 09:44:00'),
  (8,  'Colombia',                        '2022-02-15 09:44:00'),
  (9,  'Egypt',                           '2022-02-15 09:44:00'),
  (10, 'France',                          '2022-02-15 09:44:00'),
  (11, 'Germany',                         '2022-02-15 09:44:00'),
  (12, 'India',                           '2022-02-15 09:44:00'),
  (13, 'Indonesia',                       '2022-02-15 09:44:00'),
  (14, 'Iran',                            '2022-02-15 09:44:00'),
  (15, 'Italy',                           '2022-02-15 09:44:00'),
  (16, 'Japan',                           '2022-02-15 09:44:00'),
  (17, 'Mexico',                          '2022-02-15 09:44:00'),
  (18, 'Nigeria',                         '2022-02-15 09:44:00'),
  (19, 'Peru',                            '2022-02-15 09:44:00'),
  (20, 'Spain',                           '2022-02-15 09:44:00'),
  (21, 'United Kingdom',                  '2022-02-15 09:44:00'),
  (22, 'United States',                   '2022-02-15 09:44:00'),
  (23, 'Venezuela',                       '2022-02-15 09:44:00');
SELECT setval('country_country_id_seq', 23);

-- ============================================================
-- CITY
-- ============================================================
INSERT INTO city (city_id, city, country_id, last_update) VALUES
  (1,   'Buenos Aires',  3,  '2022-02-15 09:45:25'),
  (2,   'Sydney',        4,  '2022-02-15 09:45:25'),
  (3,   'Rio de Janeiro',5,  '2022-02-15 09:45:25'),
  (4,   'Toronto',       6,  '2022-02-15 09:45:25'),
  (5,   'Beijing',       7,  '2022-02-15 09:45:25'),
  (6,   'Bogota',        8,  '2022-02-15 09:45:25'),
  (7,   'Cairo',         9,  '2022-02-15 09:45:25'),
  (8,   'Paris',         10, '2022-02-15 09:45:25'),
  (9,   'Berlin',        11, '2022-02-15 09:45:25'),
  (10,  'Mumbai',        12, '2022-02-15 09:45:25'),
  (11,  'Jakarta',       13, '2022-02-15 09:45:25'),
  (12,  'Tehran',        14, '2022-02-15 09:45:25'),
  (13,  'Rome',          15, '2022-02-15 09:45:25'),
  (14,  'Tokyo',         16, '2022-02-15 09:45:25'),
  (15,  'Mexico City',   17, '2022-02-15 09:45:25'),
  (16,  'Lagos',         18, '2022-02-15 09:45:25'),
  (17,  'Lima',          19, '2022-02-15 09:45:25'),
  (18,  'Madrid',        20, '2022-02-15 09:45:25'),
  (19,  'London',        21, '2022-02-15 09:45:25'),
  (20,  'New York',      22, '2022-02-15 09:45:25'),
  (21,  'Los Angeles',   22, '2022-02-15 09:45:25'),
  (22,  'Caracas',       23, '2022-02-15 09:45:25');
SELECT setval('city_city_id_seq', 22);

-- ============================================================
-- ADDRESS
-- ============================================================
INSERT INTO address (address_id, address, address2, district, city_id, postal_code, phone, last_update) VALUES
  (1, '47 MySakila Drive',    NULL, 'Alberta',   4,  NULL,   '14033335568',  '2022-02-15 09:45:30'),
  (2, '28 MySQL Boulevard',   NULL, 'QLD',       2,  NULL,   '6172235589',   '2022-02-15 09:45:30'),
  (3, '742 Evergreen Terrace',NULL, 'New York',  20, '10001', '5551234567',   '2022-02-15 09:45:30'),
  (4, '221B Baker Street',    NULL, 'London',    19, 'NW1 6XE','5559876543',  '2022-02-15 09:45:30'),
  (5, '1600 Pennsylvania Ave',NULL, 'District',  21, '20500', '5551112222',   '2022-02-15 09:45:30');
SELECT setval('address_address_id_seq', 5);

-- ============================================================
-- ACTOR (sample)
-- ============================================================
INSERT INTO actor (actor_id, first_name, last_name, last_update) VALUES
  (1,  'PENELOPE',  'GUINESS',       '2022-02-15 09:34:33'),
  (2,  'NICK',      'WAHLBERG',      '2022-02-15 09:34:33'),
  (3,  'ED',        'CHASE',         '2022-02-15 09:34:33'),
  (4,  'JENNIFER',  'DAVIS',         '2022-02-15 09:34:33'),
  (5,  'JOHNNY',    'LOLLOBRIGIDA',  '2022-02-15 09:34:33'),
  (6,  'BETTE',     'NICHOLSON',     '2022-02-15 09:34:33'),
  (7,  'GRACE',     'MOSTEL',        '2022-02-15 09:34:33'),
  (8,  'MATTHEW',   'JOHANSSON',     '2022-02-15 09:34:33'),
  (9,  'JOE',       'SWANK',         '2022-02-15 09:34:33'),
  (10, 'CHRISTIAN', 'GABLE',         '2022-02-15 09:34:33');
SELECT setval('actor_actor_id_seq', 10);

-- ============================================================
-- FILM (sample)
-- ============================================================
INSERT INTO film (film_id, title, description, release_year, language_id, rental_duration, rental_rate, length, replacement_cost, rating, fulltext, last_update) VALUES
  (1, 'ACADEMY DINOSAUR', 'A Epic Drama of a Feminist And a Mad Scientist who must Battle a Teacher in The Canadian Rockies', 2006, 1, 6, 0.99, 86, 20.99, 'PG', to_tsvector('english', 'ACADEMY DINOSAUR Epic Drama Feminist Mad Scientist Battle Teacher Canadian Rockies'), '2022-02-15 10:05:03'),
  (2, 'ACE GOLDFINGER', 'A Astounding Epistle of a Database Administrator And a Explorer who must Find a Car in Ancient China', 2006, 1, 3, 4.99, 48, 12.99, 'G', to_tsvector('english', 'ACE GOLDFINGER Astounding Epistle Database Administrator Explorer Find Car Ancient China'), '2022-02-15 10:05:03'),
  (3, 'ADAPTATION HOLES', 'A Astounding Reflection of a Lumberjack And a Car who must Sink a Lumberjack in A Baloon Factory', 2006, 1, 7, 2.99, 50, 18.99, 'NC-17', to_tsvector('english', 'ADAPTATION HOLES Astounding Reflection Lumberjack Car Sink Baloon Factory'), '2022-02-15 10:05:03'),
  (4, 'AFFAIR PREJUDICE', 'A Fanciful Documentary of a Frisbee And a Lumberjack who must Chase a Monkey in A Shark Tank', 2006, 1, 5, 2.99, 117, 26.99, 'G', to_tsvector('english', 'AFFAIR PREJUDICE Fanciful Documentary Frisbee Lumberjack Chase Monkey Shark Tank'), '2022-02-15 10:05:03'),
  (5, 'AFRICAN EGG', 'A Fast-Paced Documentary of a Pastry Chef And a Dentist who must Pursue a Forensic Psychologist in The Gulf of Mexico', 2006, 1, 6, 2.99, 130, 22.99, 'G', to_tsvector('english', 'AFRICAN EGG Fast-Paced Documentary Pastry Chef Dentist Pursue Forensic Psychologist Gulf Mexico'), '2022-02-15 10:05:03');
SELECT setval('film_film_id_seq', 5);

-- ============================================================
-- FILM_ACTOR
-- ============================================================
INSERT INTO film_actor (actor_id, film_id, last_update) VALUES
  (1, 1, '2022-02-15 10:05:03'),
  (2, 1, '2022-02-15 10:05:03'),
  (3, 2, '2022-02-15 10:05:03'),
  (4, 2, '2022-02-15 10:05:03'),
  (5, 3, '2022-02-15 10:05:03'),
  (6, 3, '2022-02-15 10:05:03'),
  (7, 4, '2022-02-15 10:05:03'),
  (8, 4, '2022-02-15 10:05:03'),
  (9, 5, '2022-02-15 10:05:03'),
  (10, 5, '2022-02-15 10:05:03');

-- ============================================================
-- FILM_CATEGORY
-- ============================================================
INSERT INTO film_category (film_id, category_id, last_update) VALUES
  (1, 1, '2022-02-15 10:07:33'),
  (2, 5, '2022-02-15 10:07:33'),
  (3, 7, '2022-02-15 10:07:33'),
  (4, 14, '2022-02-15 10:07:33'),
  (5, 6, '2022-02-15 10:07:33');

-- ============================================================
-- STAFF
-- ============================================================
INSERT INTO staff (staff_id, first_name, last_name, address_id, email, store_id, active, username, password, last_update) VALUES
  (1, 'Mike',  'Hillyer', 1, 'mike@sakilastore.com', 1, true, 'Mike',  '8cb2237d0679ca88db6464eac60da96345513964', '2022-02-15 10:10:07'),
  (2, 'Jon',   'Stephens', 2, 'jon@sakilastore.com', 2, true, 'Jon',   '8cb2237d0679ca88db6464eac60da96345513964', '2022-02-15 10:10:07');
SELECT setval('staff_staff_id_seq', 2);

-- ============================================================
-- STORE
-- ============================================================
INSERT INTO store (store_id, manager_staff_id, address_id, last_update) VALUES
  (1, 1, 1, '2022-02-15 10:10:07'),
  (2, 2, 2, '2022-02-15 10:10:07');
SELECT setval('store_store_id_seq', 2);

-- ============================================================
-- CUSTOMER
-- ============================================================
INSERT INTO customer (customer_id, store_id, first_name, last_name, email, address_id, activebool, create_date, active, last_update) VALUES
  (1, 1, 'MARY',       'SMITH',      'mary.smith@sakilacustomer.org', 3, true, '2022-02-15', 1, '2022-02-15 10:10:07'),
  (2, 1, 'PATRICIA',   'JOHNSON',    'patricia.johnson@sakilacustomer.org', 4, true, '2022-02-15', 1, '2022-02-15 10:10:07'),
  (3, 2, 'LINDA',      'WILLIAMS',   'linda.williams@sakilacustomer.org', 5, true, '2022-02-15', 1, '2022-02-15 10:10:07');
SELECT setval('customer_customer_id_seq', 3);

-- ============================================================
-- INVENTORY
-- ============================================================
INSERT INTO inventory (inventory_id, film_id, store_id, last_update) VALUES
  (1, 1, 1, '2022-02-15 10:10:07'),
  (2, 1, 1, '2022-02-15 10:10:07'),
  (3, 2, 1, '2022-02-15 10:10:07'),
  (4, 3, 1, '2022-02-15 10:10:07'),
  (5, 4, 2, '2022-02-15 10:10:07'),
  (6, 5, 2, '2022-02-15 10:10:07');
SELECT setval('inventory_inventory_id_seq', 6);

-- ============================================================
-- RENTAL
-- ============================================================
INSERT INTO rental (rental_id, rental_date, inventory_id, customer_id, return_date, staff_id, last_update) VALUES
  (1, '2022-02-15 11:00:00', 1, 1, '2022-02-20 11:00:00', 1, '2022-02-15 11:00:00'),
  (2, '2022-02-16 12:00:00', 3, 2, '2022-02-22 12:00:00', 1, '2022-02-16 12:00:00'),
  (3, '2022-02-17 14:00:00', 4, 3, NULL, 2, '2022-02-17 14:00:00');
SELECT setval('rental_rental_id_seq', 3);

-- ============================================================
-- PAYMENT
-- ============================================================
INSERT INTO payment (payment_id, customer_id, staff_id, rental_id, amount, payment_date) VALUES
  (1, 1, 1, 1, 5.99, '2022-02-20 11:00:00'),
  (2, 2, 1, 2, 4.99, '2022-02-22 12:00:00'),
  (3, 3, 2, 3, 2.99, '2022-02-17 14:00:00');
SELECT setval('payment_payment_id_seq', 3);

-- ============================================================
-- NOTE: For the full Sakila dataset (200 actors, 1000+ films,
-- 600+ customers, 16000+ rentals), run:
--   psql -d sakila -f db/pagila-data.sql
-- Download from: https://raw.githubusercontent.com/devrimgunduz/pagila/master/pagila-data.sql
-- ============================================================
