CREATE DATABASE IF NOT EXISTS toko_berkah;
USE toko_berkah;

CREATE TABLE IF NOT EXISTS tb_user (
    id_user INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nama_lengkap VARCHAR(100) NOT NULL,
    role ENUM('admin', 'kasir') NOT NULL DEFAULT 'kasir',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tb_kategori (
    id_kategori VARCHAR(10) PRIMARY KEY,
    nama_kategori VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_barang (
    id_barang VARCHAR(20) PRIMARY KEY,
    id_kategori VARCHAR(10) NULL,
    nama_barang VARCHAR(100) NOT NULL,
    satuan VARCHAR(20) NULL,
    harga_jual DECIMAL(12,2) NOT NULL DEFAULT 0,
    stok INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_barang_kategori
        FOREIGN KEY (id_kategori) REFERENCES tb_kategori(id_kategori)
        ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS tb_customer (
    id_customer VARCHAR(20) PRIMARY KEY,
    nama_customer VARCHAR(100) NOT NULL,
    alamat VARCHAR(255),
    telepon VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS tb_penjualan (
    id_jual INT AUTO_INCREMENT PRIMARY KEY,
    tgl_transaksi DATE NOT NULL,
    id_customer VARCHAR(20) NOT NULL,
    id_barang VARCHAR(20) NOT NULL,
    jumlah_beli INT NOT NULL,
    total_bayar DECIMAL(12,2) NOT NULL DEFAULT 0,
    id_user INT NOT NULL,
    CONSTRAINT fk_penjualan_customer
        FOREIGN KEY (id_customer) REFERENCES tb_customer(id_customer)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_penjualan_barang
        FOREIGN KEY (id_barang) REFERENCES tb_barang(id_barang)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_penjualan_user
        FOREIGN KEY (id_user) REFERENCES tb_user(id_user)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

INSERT INTO tb_user (id_user, username, password, nama_lengkap, role) VALUES
    (1, 'admin', 'admin123', 'Administrator Toko', 'admin'),
    (2, 'kasir01', 'kasir123', 'Kasir Utama', 'kasir')
ON DUPLICATE KEY UPDATE
    username = VALUES(username),
    password = VALUES(password),
    nama_lengkap = VALUES(nama_lengkap),
    role = VALUES(role);

INSERT INTO tb_kategori (id_kategori, nama_kategori) VALUES
    ('KT01', 'Sembako'),
    ('KT02', 'Minuman'),
    ('KT03', 'Snack'),
    ('KT04', 'Kebutuhan Rumah')
ON DUPLICATE KEY UPDATE
    nama_kategori = VALUES(nama_kategori);

INSERT INTO tb_barang (id_barang, id_kategori, nama_barang, satuan, harga_jual, stok) VALUES
    ('BRG001', 'KT01', 'Beras Premium 5kg', 'karung', 78000, 25),
    ('BRG002', 'KT01', 'Gula Pasir 1kg', 'pcs', 16500, 50),
    ('BRG003', 'KT02', 'Minyak Goreng 1L', 'botol', 18500, 40),
    ('BRG004', 'KT02', 'Teh Celup 25s', 'kotak', 9500, 35),
    ('BRG005', 'KT03', 'Biskuit Coklat', 'pcs', 8500, 60),
    ('BRG006', 'KT04', 'Sabun Cuci Piring', 'botol', 12000, 45)
ON DUPLICATE KEY UPDATE
    id_kategori = VALUES(id_kategori),
    nama_barang = VALUES(nama_barang),
    satuan = VALUES(satuan),
    harga_jual = VALUES(harga_jual),
    stok = VALUES(stok);

INSERT INTO tb_customer (id_customer, nama_customer, alamat, telepon) VALUES
    ('CUST001', 'Andi Saputra', 'Serang, Banten', '081234560001'),
    ('CUST002', 'Budi Santoso', 'Cilegon, Banten', '081234560002'),
    ('CUST003', 'Siti Rahma', 'Pandeglang, Banten', '081234560003'),
    ('CUST004', 'Dewi Lestari', 'Rangkasbitung, Banten', '081234560004')
ON DUPLICATE KEY UPDATE
    nama_customer = VALUES(nama_customer),
    alamat = VALUES(alamat),
    telepon = VALUES(telepon);

INSERT INTO tb_penjualan (tgl_transaksi, id_customer, id_barang, jumlah_beli, total_bayar, id_user) VALUES
    (CURDATE() - INTERVAL 2 DAY, 'CUST001', 'BRG001', 1, 78000, 1),
    (CURDATE() - INTERVAL 1 DAY, 'CUST002', 'BRG003', 2, 37000, 2),
    (CURDATE(), 'CUST003', 'BRG005', 3, 25500, 2);
