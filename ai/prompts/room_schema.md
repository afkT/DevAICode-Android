# Room Schema (Tables and DAO)

This prompt defines how to add or change Room entities, DAOs, database, and migrations. It applies to projects with data/domain layers and Hilt. Scope, rules, and required operations are stated only in this file.

---

## Scope

- New or changed tables, DAOs, TypeConverter, database version and migrations. Keep domain models separate from Room entities; map at boundaries.

---

## Entity

- **Annotations** — @Entity(tableName = "xxx"); @PrimaryKey (autoGenerate = true or business key); primaryKeys = [...] for composite.
- **Columns** — @ColumnInfo(name = "column_name") with lowercase, underscore; avoid mismatch with Kotlin property names.
- **Types** — Room supports primitives, String, ByteArray; for date, enum, collections use **TypeConverter** or @TypeConverters; complex objects as JSON string or separate table.
- **Indexes** — @Index or @Entity(indices = [...]) on query, JOIN, ORDER BY columns to avoid full scan.
- **Boundary** — Entity stays in data layer; do not expose in domain or UI; map to domain in mapper.

---

## DAO (Data Access Object)

- **Location** — DAO interface in data layer (e.g. data.local.dao); provide implementation via Hilt (Room generates it).
- **Methods** — @Insert, @Update, @Delete for writes (suspend or return); prefer **Flow** for reactive reads, suspend for one-shot.
- **Queries** — Use placeholders (:param or ?); never concatenate SQL. For complex queries use multiple methods or @RawQuery with strict input control.
- **Transactions** — Use @Transaction for multi-step writes so they are atomic.

---

## Database

- **Annotations** — @Database(entities = [...], version = N, exportSchema = true). **exportSchema = true** so schema JSON is generated for migrations.
- **TypeConverters** — Register with @TypeConverters(Xxx::class) on @Database; converter methods static or singleton, thread-safe.
- **Singleton** — Provide Database via Hilt @Singleton; expose abstract methods for each DAO; Room implements.
- **fallbackToDestructiveMigration** — Use only when there is no user data or when data loss is acceptable (e.g. dev/test). Production must use Migration to avoid data loss.

---

## Migration

- **Version** — Increment version on any schema or index change; in Migration.migrate() run SQL (e.g. ALTER TABLE, create new table and copy data).
- **Schema file** — Use Room’s exported JSON schema (build output) to see diff and write correct SQL; avoid dropping columns or losing data by mistake.
- **Tests** — Test migration: create DB at old version, run Migration, assert new schema and essential data; use JUnit, runTest or Instrumentation as needed.
- **Rollback** — Document when migration is one-way; for major upgrades consider backup or export/import.

---

## Repository and boundary

- **Interface** — Repository interface in **domain** (e.g. domain.repository.XxxRepository); methods take primitives or domain types, return Flow&lt;T&gt;, suspend, or Result&lt;T&gt;.
- **Implementation** — In **data** (e.g. data.repository.XxxRepositoryImpl); use DAO, map Entity to domain, return; do not reference Room/Entity in interface or domain.
- **Hilt** — In data module @Binds Repository interface to implementation; @Provides Database and DAO.

---

## Example request (for AI)

```
Add table Yyy: id (primary key auto), name, createdAt (Long).
- Add YyyEntity, YyyDao (insert, listAll as Flow), YyyRepository interface and implementation.
- Register entity and DAO in Database; bump version N to N+1.
- If there was a previous version, add Migration and in-memory migration test.
```
