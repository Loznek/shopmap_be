-- Create Map table
CREATE TABLE Map (
                     id SERIAL PRIMARY KEY,
                     width DOUBLE PRECISION,
                     height DOUBLE PRECISION,
                     entrancex DOUBLE PRECISION,
                     entrancey DOUBLE PRECISION,
                     exitx DOUBLE PRECISION,
                     exity DOUBLE PRECISION,
                     storeid INT REFERENCES Store(id)


);

-- Create Department table
CREATE TABLE Department (
                            id SERIAL PRIMARY KEY,
                            MapId INT REFERENCES Map(Id),
                            Name TEXT,
                            Width DOUBLE PRECISION,
                            Height DOUBLE PRECISION,
                            StartX DOUBLE PRECISION,
                            StartY DOUBLE PRECISION
);

-- Create an index on Department's foreign key (MapId)
CREATE INDEX idx_department_mapid ON Department(MapId);

-- Create Shelf table
CREATE TABLE Shelf (
                       id SERIAL PRIMARY KEY,
                       departmentid INT REFERENCES Department(Id),
                       width DOUBLE PRECISION,
                       height DOUBLE PRECISION,
                       startx DOUBLE PRECISION,
                       starty DOUBLE PRECISION
);

-- Create an index on Shelf's foreign key (DepartmentId)
CREATE INDEX idx_shelf_departmentid ON Shelf(DepartmentId);

-- Create Product table
CREATE TABLE Product (
                         article_no SERIAL PRIMARY KEY,
                         name TEXT,
                         size INT,
                         shelfid INT REFERENCES Shelf(Id),
                         price DOUBLE PRECISION
);

-- Create an index on Product's foreign key (ShelfId)
CREATE INDEX idx_product_shelfid ON Product(ShelfId);

-- Create Store table
CREATE TABLE Store (
                       id SERIAL PRIMARY KEY,
                       name TEXT,
                       location TEXT,

);

-- Create an index on Store's foreign key (MapId)
-- CREATE INDEX idx_store_mapid ON Store(MapId);

-- Create Till table
CREATE TABLE Till (
                      id SERIAL PRIMARY KEY,
                      mapid INT REFERENCES Map(Id),
                      width DOUBLE PRECISION,
                      height DOUBLE PRECISION,
                      startx DOUBLE PRECISION,
                      starty DOUBLE PRECISION
);

-- Create an index on Till's foreign key (MapId)
CREATE INDEX idx_till_mapid ON Till(MapId);

-- Create Wall_Block table
CREATE TABLE wall_block (
                            id SERIAL PRIMARY KEY,
                            mapid INT REFERENCES Map(Id),
                            width DOUBLE PRECISION,
                            height DOUBLE PRECISION,
                            startx DOUBLE PRECISION,
                            starty DOUBLE PRECISION
);

-- Create an index on Wall_Block's foreign key (MapId)
CREATE INDEX idx_wallblock_mapid ON Wall_Block(MapId);

CREATE TABLE google_maps_info
(
    id                    SERIAL PRIMARY KEY,
    storeid               INT     NOT NULL,

    placeid               TEXT    NOT NULL,
    phone_number          TEXT,
    website_uri           TEXT,
    google_maps_uri       TEXT,

    rating                DOUBLE PRECISION,
    user_rating_count     INT,

    has_parking           BOOLEAN NOT NULL,
    wheelchair_accessible BOOLEAN NOT NULL,


    CONSTRAINT fk_google_store
        FOREIGN KEY (storeid)
            REFERENCES store (id)
            ON DELETE CASCADE
);

CREATE TABLE opening_hours (
                               id SERIAL PRIMARY KEY,
                               storeid INT NOT NULL,

                               day INT NOT NULL,
                               open_time TEXT NOT NULL,
                               close_time TEXT NOT NULL,

                               CONSTRAINT fk_opening_store
                                   FOREIGN KEY (storeid)
                                       REFERENCES store(id)
                                       ON DELETE CASCADE
);


CREATE TABLE store_pictures (
                                id SERIAL PRIMARY KEY,
                                storeid INT NOT NULL,

                                path TEXT NOT NULL,

                                CONSTRAINT fk_picture_store
                                    FOREIGN KEY (storeid)
                                        REFERENCES store(id)
                                        ON DELETE CASCADE
);

CREATE INDEX idx_google_store_id ON google_maps_info(storeid);
CREATE INDEX idx_opening_store_id ON opening_hours(storeid);
CREATE INDEX idx_pictures_store_id ON store_pictures(storeid);

