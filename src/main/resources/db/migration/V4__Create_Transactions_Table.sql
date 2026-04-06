
CREATE TABLE transactions (
	id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	amount DECIMAL(19,2) NOT NULL,
	description VARCHAR(255),
	type VARCHAR(50) NOT NULL,
	
	target_wallet_id BIGINT,
	wallet_id BIGINT NOT NULL,
	
	category_id UUID,
	user_id UUID NOT NULL,
	
	transaction_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	
	CONSTRAINT fk_target_wallet FOREIGN KEY(target_wallet_id) REFERENCES wallets(id),
	CONSTRAINT fk_wallet FOREIGN KEY(wallet_id) REFERENCES wallets(id),
	
	CONSTRAINT fk_category FOREIGN KEY(category_id) REFERENCES category(id),
	CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
)