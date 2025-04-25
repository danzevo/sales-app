-- Step 1: Add column (NULLABLE first, no default to avoid duplicates)
ALTER TABLE users ADD COLUMN email VARCHAR(100);

-- Step 2 (optional): Manually update emails for each existing user
-- e.g., using an UPDATE for existing users with real email data
-- UPDATE users SET email = 'user1@example.com' WHERE id = 1;
-- UPDATE users SET email = 'user2@example.com' WHERE id = 2;

-- Step 3: Delete or fix duplicates, OR use placeholder unique emails
-- Here's a simple example (for development/testing):
UPDATE users
SET email = CONCAT('user_', id, '@example.com')
WHERE email IS NULL;

-- Step 4: Make column NOT NULL
ALTER TABLE users
    ALTER COLUMN email SET NOT NULL;

-- Step 5: Add unique index
CREATE UNIQUE INDEX users_email_key ON users(email);
