CREATE EXTENSION IF NOT EXISTS pgcrypto;


CREATE TABLE IF NOT EXISTS airline
(
    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid()   NOT NULL,
    name            VARCHAR(100)    UNIQUE                                  NOT NULL,
    description     VARCHAR(200)                                            NOT NULL,
    created_at      TIMESTAMPTZ                 DEFAULT NOW()               NOT NULL,
    updated_at      TIMESTAMPTZ                 DEFAULT NOW()               NOT NULL,
    deleted         BOOLEAN                     DEFAULT false               NOT NULL,
    deleted_at      TIMESTAMPTZ,
    created_by      VARCHAR(50),
    modified_by     VARCHAR(50)
);


CREATE TABLE IF NOT EXISTS customer
(
    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid()   NOT NULL,
    name            VARCHAR(50)     UNIQUE                                  NOT NULL,
    location        VARCHAR(100)                                            NOT NULL,
    email           VARCHAR(100)                                            NOT NULL,
    phone_number    VARCHAR(50)                                             NOT NULL,
    description     VARCHAR(200)                                            NOT NULL,
    created_at      TIMESTAMPTZ                 DEFAULT NOW()               NOT NULL,
    updated_at      TIMESTAMPTZ                 DEFAULT NOW()               NOT NULL,
    deleted         BOOLEAN                     DEFAULT false               NOT NULL,
    deleted_at      TIMESTAMPTZ,
    created_by      VARCHAR(50),
    modified_by     VARCHAR(50)
);


CREATE TABLE IF NOT EXISTS product_category
(
    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid()   NOT NULL,
    name            VARCHAR(100)    UNIQUE                                  NOT NULL,
    description     VARCHAR(200)                                            NOT NULL,
    created_at      TIMESTAMPTZ                 DEFAULT NOW()               NOT NULL,
    updated_at      TIMESTAMPTZ                 DEFAULT NOW()               NOT NULL,
    deleted         BOOLEAN                     DEFAULT false               NOT NULL,
    deleted_at      TIMESTAMPTZ,
    created_by      VARCHAR(50),
    modified_by     VARCHAR(50)
);


CREATE TABLE IF NOT EXISTS product
(
    id                      UUID            PRIMARY KEY DEFAULT gen_random_uuid()               NOT NULL,
    name                    VARCHAR(100)    UNIQUE                                              NOT NULL,
    description             VARCHAR(200)                                                        NOT NULL,
    quantity_per_pack       INTEGER                     DEFAULT 0                               NOT NULL,
    weight                  DECIMAL(6, 3)               DEFAULT 0.00                            NOT NULL,
    measurement_units       VARCHAR(50)     CHECK (measurement_units IN ('KILOGRAM','GRAM'))    NOT NULL,
    product_category_id     UUID            REFERENCES   product_category(id)                   NOT NULL,
    weight_per_pack_in_kg   DECIMAL(6, 3)                                                       NOT NULL,
    created_at              TIMESTAMPTZ                 DEFAULT NOW()                           NOT NULL,
    deleted                 BOOLEAN                     DEFAULT false                           NOT NULL,
    deleted_at              TIMESTAMPTZ,
    created_by              VARCHAR(50),
    modified_by             VARCHAR(50)
);


CREATE TABLE IF NOT EXISTS product_order
(
    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid()   NOT NULL,
    name            VARCHAR(100)    UNIQUE                                  NOT NULL,
    quantity        INTEGER                     DEFAULT 0                   NOT NULL,
    description     VARCHAR(200)                                            NOT NULL,
    product_id      UUID            REFERENCES   product(id)                NOT NULL,
    airline_id      UUID            REFERENCES   airline(id)                NOT NULL,
    customer_id     UUID            REFERENCES   customer(id)               NOT NULL,
    created_at      TIMESTAMPTZ                 DEFAULT NOW()               NOT NULL,
    updated_at      TIMESTAMPTZ                 DEFAULT NOW()               NOT NULL,
    deleted         BOOLEAN                     DEFAULT false               NOT NULL,
    deleted_at      TIMESTAMPTZ,
    created_by      VARCHAR(50),
    modified_by     VARCHAR(50),
    flight          VARCHAR(50),
);
