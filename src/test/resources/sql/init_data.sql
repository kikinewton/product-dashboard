INSERT INTO airline (id, description, name) VALUES ('094551bd-881a-474a-b652-44a4cddbf3fb','KLM','KLM');
INSERT INTO airline (id, description, name) VALUES ('4ae0dea8-78c8-4595-b24d-65e944067a6a','SAS','SAS');

INSERT INTO product_category (id, description, name) VALUES ('d81165f0-b519-4574-9c7b-845393c6e993','Fresh fruits','fruits');
INSERT INTO product_category (id, description, name) VALUES ('473d8a91-3a54-44d9-8454-71a749d5d89f','Fresh nuts','Nuts');

INSERT INTO customer (id, name, description, email, location, phone_number)
VALUES ('2cd4dcae-3a41-4194-9e0d-0cef9501a5f9', 'Sainsbury', 'Food store', 'support@sainsbury.com','Leeds', '00000');


INSERT INTO product (id, name, description, measurement_units, product_category_id, quantity_per_pack, weight, pack_weight_in_kg)
VALUES ('e9a4b64c-71ab-451a-8aed-b2598b9ff5f1', 'Mango cuts', 'Freshly cut', 'GRAM', 'd81165f0-b519-4574-9c7b-845393c6e993', 4, '240', 0.96);

INSERT INTO product (id, name, description, measurement_units, product_category_id, quantity_per_pack, weight, pack_weight_in_kg)
VALUES (gen_random_uuid(), 'Mango strips', 'Freshly cut', 'GRAM', 'd81165f0-b519-4574-9c7b-845393c6e993', 4, '240', 0.96);


INSERT INTO product_order (id, airline_id, customer_id, description, flight, required_date)
VALUES ('d51d3f24-8ad7-43b2-87ac-27b1d03c0a1e','094551bd-881a-474a-b652-44a4cddbf3fb','2cd4dcae-3a41-4194-9e0d-0cef9501a5f9',
        'Product order A', 'M0DD2', '2024-03-03T22:00:00Z');

INSERT INTO order_detail (product_id,quantity,id)
VALUES ('e9a4b64c-71ab-451a-8aed-b2598b9ff5f1', 10, '0f7231c8-e551-4278-96be-b9292c1ea130');

INSERT INTO product_order (id, airline_id, customer_id, description, flight, required_date)
VALUES ('aebc1f59-3248-421f-b0c4-c26fb5d5f507','094551bd-881a-474a-b652-44a4cddbf3fb','2cd4dcae-3a41-4194-9e0d-0cef9501a5f9',
        'Order mango cuts', 'GR4FF', '2024-03-05T22:00:00Z');

INSERT INTO product_order_order_detail (product_order_id,order_detail_id)
VALUES ('aebc1f59-3248-421f-b0c4-c26fb5d5f507', '0f7231c8-e551-4278-96be-b9292c1ea130');

insert into order_fulfillment (id, created_by,product_id,product_order_id,quantity)
VALUES ('5434746f-53bc-4db5-af8d-336ee0f4ff0c', 'test@mail.com', 'e9a4b64c-71ab-451a-8aed-b2598b9ff5f1', 'aebc1f59-3248-421f-b0c4-c26fb5d5f507', 32);
