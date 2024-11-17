-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 10, 2024 at 05:26 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `bookshop`
--

-- --------------------------------------------------------

--
-- Table structure for table `customer_records`
--

CREATE TABLE `customer_records` (
  `customer_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `loyalty_status` varchar(50) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customer_records`
--

INSERT INTO `customer_records` (`customer_id`, `name`, `address`, `phone_number`, `email`, `loyalty_status`, `created_at`) VALUES
(1, 'sandun sahiru', 'walaliya', '0764162860', 'sandun@gg.com', 'premium', '2024-11-05 04:44:18');

-- --------------------------------------------------------

--
-- Table structure for table `sales_records`
--

CREATE TABLE `sales_records` (
  `sale_id` int(11) NOT NULL,
  `book_title` varchar(100) NOT NULL,
  `quantity` int(11) NOT NULL,
  `customer_id` varchar(255) NOT NULL,
  `stock_id` varchar(255) NOT NULL,
  `price_per_unit` int(50) NOT NULL,
  `total_amount` int(50) NOT NULL,
  `sale_date` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `sales_records`
--

INSERT INTO `sales_records` (`sale_id`, `book_title`, `quantity`, `customer_id`, `stock_id`, `price_per_unit`, `total_amount`, `sale_date`) VALUES
(24, '13', 25, '1', '102', 350, 8750, '2024-11-10 03:43:53'),
(25, '34', 20, '5', '103', 300, 6000, '2024-11-10 04:22:35');

-- --------------------------------------------------------

--
-- Table structure for table `stock_records`
--

CREATE TABLE `stock_records` (
  `stock_id` varchar(20) NOT NULL,
  `supplier_id` varchar(20) NOT NULL,
  `book_id` varchar(20) NOT NULL,
  `author_name` varchar(100) NOT NULL,
  `book_name` varchar(100) NOT NULL,
  `market_price` int(50) NOT NULL,
  `selling_price` int(50) NOT NULL,
  `quantity` int(50) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `stock_records`
--

INSERT INTO `stock_records` (`stock_id`, `supplier_id`, `book_id`, `author_name`, `book_name`, `market_price`, `selling_price`, `quantity`, `created_at`) VALUES
('103', '4', '56', 'mendis', 'holmes', 300, 325, 30, '2024-11-10 04:20:51');

-- --------------------------------------------------------

--
-- Table structure for table `supplier_records`
--

CREATE TABLE `supplier_records` (
  `supplier_id` varchar(20) NOT NULL,
  `supplier_name` varchar(100) NOT NULL,
  `supplier_address` varchar(255) NOT NULL,
  `supplier_email` varchar(100) NOT NULL,
  `supplier_phone` varchar(20) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `supplier_records`
--

INSERT INTO `supplier_records` (`supplier_id`, `supplier_name`, `supplier_address`, `supplier_email`, `supplier_phone`, `created_at`) VALUES
('24', 'anne', 'colombo 05', 'anne@gmail.com', '0712345645', '2024-11-10 04:19:49');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `salt` varchar(255) NOT NULL,
  `role` enum('ADMIN','STAFF','USER') DEFAULT 'USER',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `profile_photo` longblob DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `full_name`, `email`, `phone_number`, `password`, `salt`, `role`, `created_at`, `profile_photo`) VALUES
(1, 'sandun', 'almongoguru@gmail.com', '0764162860', 'n8mz6ySV6uTp+h2Ot1B6laDrnF6qioYFrEwWAUIVEMM=', '1bOXbc9+r1LyiaQIwaU7Nw==', 'USER', '2024-11-05 02:10:54', NULL),
(3, 'Sahiru', 'almongoguru1@gmail.com', '0764162860', 'sLiX6BW4BXDWirppkkPyu6xKKIrOTExKrCKbQNSHJdY=', 'O8PbFDEArmEoGgnoXiDG3Q==', 'USER', '2024-11-05 02:22:32', NULL),
(7, 'P.M.K Saman Perera', 'samanperera@gmail.com', '0717652356', 'JeeZD0Wd6fKnHrbPj4KkrPtMIm+Vmw1zRpl+EfJxAQ4=', '7lwcTwcgVP4P/lzp16EKEg==', 'USER', '2024-11-10 01:24:47', NULL),
(8, 'M.A.K Saman Dissanayake', 'samandissanayake@gmail.com', '0717542356', 'youPFxZboBOGLLtKMmJRbT5hOzuZeReAciBvyPLI3ZM=', 'GjufAz7+lei7321Ffdn8OQ==', 'USER', '2024-11-10 01:35:52', NULL),
(10, 'M.K.P Kamal Perera', 'kamalperera@gmail.com', '0714562345', 'PooPuZUJwLbKwnLIrNOhv0fXtZnV329pGfEAsTrtU2Y=', 'feDt68p+k2v2xKbBjd5PRQ==', 'USER', '2024-11-10 03:25:16', NULL),
(12, 'P.A.S Saman Silva', 'samansilva@gmail.com', '0712345678', '9rwBSRtMXc7vt1CRof+wCh8MJ9V64Ph0OH/LoOy/kmE=', '7KnB9XWCGrH72g+qb94w/Q==', 'USER', '2024-11-10 03:31:58', NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `customer_records`
--
ALTER TABLE `customer_records`
  ADD PRIMARY KEY (`customer_id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `sales_records`
--
ALTER TABLE `sales_records`
  ADD PRIMARY KEY (`sale_id`);

--
-- Indexes for table `stock_records`
--
ALTER TABLE `stock_records`
  ADD UNIQUE KEY `book_id` (`book_id`);

--
-- Indexes for table `supplier_records`
--
ALTER TABLE `supplier_records`
  ADD PRIMARY KEY (`supplier_id`),
  ADD UNIQUE KEY `supplier_email` (`supplier_email`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `customer_records`
--
ALTER TABLE `customer_records`
  MODIFY `customer_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `sales_records`
--
ALTER TABLE `sales_records`
  MODIFY `sale_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
