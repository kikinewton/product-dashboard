CREATE TABLE IF NOT EXISTS app_user
(
   id                           UUID        PRIMARY KEY DEFAULT gen_random_uuid()       NOT NULL,
   first_name                   VARCHAR(50)                                             NOT NULL,
   last_name                    VARCHAR(50)                                             NOT NULL,
   password                     VARCHAR(255)                                            NOT NULL,
   email                        VARCHAR(50)         UNIQUE                              NOT NULL,
   last_login                   TIMESTAMPTZ,
   changed_default_password     BOOLEAN                 DEFAULT false                   NOT NULL,
   role                         VARCHAR(30)                                             NOT NULL,
   created_at                   TIMESTAMPTZ             DEFAULT NOW(),
   updated_at                   TIMESTAMPTZ             DEFAULT NOW(),
   modified_by                  VARCHAR(50),
   deleted                      BOOLEAN                 DEFAULT false                   NOT NULL,
   deleted_at                   TIMESTAMPTZ
);
