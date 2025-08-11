ALTER table task
DROP COLUMN name;

ALTER TABLE task
RENAME COLUMN createdDate TO dueDate;
