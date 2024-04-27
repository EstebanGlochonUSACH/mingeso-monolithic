import { type FC } from 'react';
import Badge from 'react-bootstrap/Badge';

interface BadgeInputProps {
	placeholder: string,
	value?: string|null,
}

const BadgeInput: FC<BadgeInputProps> = ({ placeholder, value }) => {
	return (
		<div className="form-control d-flex align-items-center py-2">
			{value ? (
				<Badge bg="primary">{value}</Badge>
			):(
				<div className="opacity-50">{placeholder}</div>
			)}
			&nbsp;
		</div>
	);
};

export default BadgeInput;
