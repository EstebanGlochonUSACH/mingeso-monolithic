export interface ResponseObject<T> {
	error: boolean,
	message: string,
	entity: T|null,
};