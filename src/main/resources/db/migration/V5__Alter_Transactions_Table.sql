-- Add the new createdAt column with timezone support for Instant
ALTER TABLE transactions
ADD COLUMN created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP;
