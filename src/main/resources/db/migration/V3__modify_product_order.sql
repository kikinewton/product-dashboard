ALTER TABLE product_order DROP COLUMN quantity;
ALTER TABLE product_order DROP COLUMN product_id;

ALTER TABLE product_order ADD COLUMN status VARCHAR(50) NOT NULL DEFAULT 'PENDING';
ALTER TABLE product_order ADD COLUMN required_date TIMESTAMPTZ NOT NULL;
ALTER TABLE product_order ADD COLUMN delivery_date TIMESTAMPTZ     NULL;
ALTER TABLE product_order ADD COLUMN order_fulfillment_date TIMESTAMPTZ;


CREATE TABLE IF NOT EXISTS order_detail
(
    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid()   NOT NULL,
    product_id      UUID            REFERENCES   product(id)                NOT NULL,
    quantity        INTEGER                     DEFAULT 0                   NOT NULL,
    unit_price      DECIMAL(6, 3)               DEFAULT 0.00                        ,
    created_at      TIMESTAMPTZ                 DEFAULT NOW()               NOT NULL
);


CREATE TABLE IF NOT EXISTS order_fulfillment
(
    id                    UUID            PRIMARY KEY DEFAULT gen_random_uuid()   NOT NULL,
    product_id            UUID            REFERENCES   product(id)                NOT NULL,
    product_order_id      UUID            REFERENCES   product_order(id)          NOT NULL,
    quantity              INTEGER                     DEFAULT 0                   NOT NULL,
    created_at            TIMESTAMPTZ                 DEFAULT NOW()               NOT NULL,
    created_by            VARCHAR(50),
    deleted               BOOLEAN                     DEFAULT false               NOT NULL,
    deleted_at            TIMESTAMPTZ
);

CREATE TABLE IF NOT EXISTS product_order_order_detail
(
    product_order_id      UUID            REFERENCES   product_order(id)          NOT NULL,
    order_detail_id       UUID            REFERENCES   order_detail(id)           NOT NULL
);
