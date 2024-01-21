INSERT INTO airline (id, description, name) VALUES ('094551bd-881a-474a-b652-44a4cddbf3fb','KLM','KLM');
INSERT INTO airline (id, description, name) VALUES ('4ae0dea8-78c8-4595-b24d-65e944067a6a','SAS','SAS');

INSERT INTO product_category (id, description, name) VALUES ('d81165f0-b519-4574-9c7b-845393c6e993','Fresh fruits','fruits');
INSERT INTO product_category (id, description, name) VALUES ('473d8a91-3a54-44d9-8454-71a749d5d89f','Fresh nuts','Nuts');

INSERT INTO customer (id, name, description, email, location, phone_number)
values ('2cd4dcae-3a41-4194-9e0d-0cef9501a5f9', 'Sainsbury', 'Food store', 'support@sainsbury.com','Leeds', '00000');


insert into product (id, name, description, measurement_units, product_category_id, quantity_per_pack, weight, pack_weight_in_kg)
values ('e9a4b64c-71ab-451a-8aed-b2598b9ff5f1', 'Mango cuts', 'Freshly cut', 'GRAM', 'd81165f0-b519-4574-9c7b-845393c6e993', 4, '240', 0.96);