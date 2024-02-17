ALTER TABLE order_detail ADD COLUMN created_by VARCHAR(80);
ALTER TABLE order_detail ADD COLUMN modified_by VARCHAR(80);
ALTER TABLE order_detail ADD COLUMN modified_at TIMESTAMPTZ DEFAULT NOW();

ALTER TABLE order_fulfillment ADD COLUMN modified_by VARCHAR(80);
ALTER TABLE order_fulfillment ADD COLUMN modified_at TIMESTAMPTZ DEFAULT NOW();