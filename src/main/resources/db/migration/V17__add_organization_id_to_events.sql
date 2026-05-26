ALTER TABLE events
ADD COLUMN organization_id BIGINT;

ALTER TABLE events
ADD CONSTRAINT fk_event_organization
FOREIGN KEY (organization_id)
REFERENCES organizations(id)
ON DELETE SET NULL;