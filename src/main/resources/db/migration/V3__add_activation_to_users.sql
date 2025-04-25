ALTER TABLE users ADD COLUMN activation_token VARCHAR(255);
ALTER TABLE users ADD COLUMN token_created_at TIMESTAMP;