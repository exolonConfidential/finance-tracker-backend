
CREATE table category(
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	name VARCHAR(255) NOT NULL,
	type VARCHAR(50) NOT NULL,
	-- Budget Limit (Nullable, Precision 19, Scale 2 for money)
	budget_limit DECIMAL(19,2),
	user_id UUID NOT NULL,
	CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
	CONSTRAINT uc_user_category_name UNIQUE(user_id, name)
)