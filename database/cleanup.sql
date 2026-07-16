-- ============================================
-- Library Management System
-- Database Cleanup Script
-- ============================================

-- Delete all existing records
DELETE FROM books;

-- Reset the sequence
ALTER SEQUENCE book_seq RESTART WITH 1;