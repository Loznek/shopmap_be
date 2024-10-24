-- Create Map table
CREATE TABLE Map (
                     id SERIAL PRIMARY KEY,
                     width DOUBLE PRECISION,
                     height DOUBLE PRECISION,
                     entrancex DOUBLE PRECISION,
                     entrancey DOUBLE PRECISION,
                     exitx DOUBLE PRECISION,
                     exity DOUBLE PRECISION,
                     storeid INT REFERENCES Store(id),


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
CREATE INDEX idx_store_mapid ON Store(MapId);

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
CREATE TABLE Wall_Block (
                            id SERIAL PRIMARY KEY,
                            mapid INT REFERENCES Map(Id),
                            width DOUBLE PRECISION,
                            height DOUBLE PRECISION,
                            startx DOUBLE PRECISION,
                            starty DOUBLE PRECISION
);

-- Create an index on Wall_Block's foreign key (MapId)
CREATE INDEX idx_wallblock_mapid ON Wall_Block(MapId);