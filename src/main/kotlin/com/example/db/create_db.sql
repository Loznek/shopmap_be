

CREATE TABLE store (
                       id SERIAL PRIMARY KEY,
                       name TEXT,
                       location TEXT

);



CREATE TABLE map (
                     id SERIAL PRIMARY KEY,
                     width DOUBLE PRECISION,
                     height DOUBLE PRECISION,
                     entrance_x DOUBLE PRECISION,
                     entrance_y DOUBLE PRECISION,
                     exit_x DOUBLE PRECISION,
                     exit_y DOUBLE PRECISION,
                     store_id INT UNIQUE REFERENCES Store(id)
);


CREATE TABLE department (
                            id SERIAL PRIMARY KEY,
                            map_id INT REFERENCES map(id),
                            name TEXT,
                            width DOUBLE PRECISION,
                            height DOUBLE PRECISION,
                            start_x DOUBLE PRECISION,
                            start_y DOUBLE PRECISION
);


CREATE INDEX idx_department_map_id ON department(map_id);


CREATE TABLE till (
                      id SERIAL PRIMARY KEY,
                      map_id INT REFERENCES Map(Id),
                      width DOUBLE PRECISION,
                      height DOUBLE PRECISION,
                      start_x DOUBLE PRECISION,
                      start_y DOUBLE PRECISION
);


CREATE INDEX idx_till_map_id ON till(map_id);


CREATE TABLE wall_block (
                            id SERIAL PRIMARY KEY,
                            map_id INT REFERENCES Map(Id),
                            width DOUBLE PRECISION,
                            height DOUBLE PRECISION,
                            start_x DOUBLE PRECISION,
                            start_y DOUBLE PRECISION
);


CREATE INDEX idx_wall_block_map_id ON wall_block(map_id);

CREATE TABLE google_maps_info
(
    id                    SERIAL PRIMARY KEY,
    store_id               INT     NOT NULL,
    place_id               TEXT    NOT NULL,
    phone_number          TEXT,
    website_uri           TEXT,
    google_maps_uri       TEXT,
    rating                DOUBLE PRECISION,
    user_rating_count     INT,
    has_parking           BOOLEAN NOT NULL,
    wheelchair_accessible BOOLEAN NOT NULL,
    CONSTRAINT fk_google_store
        FOREIGN KEY (store_id)
            REFERENCES store (id)
            ON DELETE CASCADE
);

CREATE TABLE opening_hours (
                               id SERIAL PRIMARY KEY,
                               store_id INT NOT NULL,
                               day INT NOT NULL,
                               open_time TEXT NOT NULL,
                               close_time TEXT NOT NULL,
                               CONSTRAINT fk_opening_store
                                   FOREIGN KEY (store_id)
                                       REFERENCES store(id)
                                       ON DELETE CASCADE
);


CREATE TABLE store_pictures (
                                id SERIAL PRIMARY KEY,
                                store_id INT NOT NULL,
                                path TEXT NOT NULL,
                                CONSTRAINT fk_picture_store
                                    FOREIGN KEY (store_id)
                                        REFERENCES store(id)
                                     ON DELETE CASCADE
);

CREATE INDEX idx_google_store_id ON google_maps_info(store_id);
CREATE INDEX idx_opening_store_id ON opening_hours(store_id);
CREATE INDEX idx_pictures_store_id ON store_pictures(store_id);



CREATE TABLE app_user (
                         id SERIAL PRIMARY KEY,
                         firebaseuid TEXT NOT NULL UNIQUE,
                         email TEXT,
                         display_name TEXT
);

CREATE TABLE shopping_list (
                              id SERIAL PRIMARY KEY,
                              user_id INT NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
                              name TEXT NOT NULL
);

CREATE TABLE shopping_list_item (
                                  id SERIAL PRIMARY KEY,
                                  shopping_list_id INT NOT NULL REFERENCES shopping_list(id) ON DELETE CASCADE,
                                  shopping_item_name TEXT NOT NULL,
                                  attributes TEXT NULL
);

CREATE INDEX idx_shopping_list_userid
    ON shopping_list(user_id);

CREATE INDEX idx_shopping_list_item_list_id
    ON shopping_list_item(shopping_list_id);

CREATE TYPE product_position AS ENUM (
    'LEFT',
    'RIGHT',
    'TOP',
    'BOTTOM'
);


CREATE TABLE product (
                         article_no SERIAL PRIMARY KEY,
                         name TEXT,
                         size TEXT,
                         department_id INT REFERENCES department(Id),
                         position product_position,
                         store_id INT REFERENCES store(Id),
                         price DOUBLE PRECISION
);


CREATE INDEX idx_product_store_id ON product(store_id);


CREATE INDEX idx_product_department_id ON product(department_id);

