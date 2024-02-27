-- drop the unit price column
ALTER TABLE order_detail DROP COLUMN unit_price;

-- add new column enabled to app_user
ALTER TABLE app_user ADD COLUMN enabled BOOLEAN DEFAULT true;