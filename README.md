# GlobeCo Trade Blotter

A microservice for managing trade orders and allowing traders to work trades as part of the GlobeCo Suite.

## Security Type API

The Security Type API provides endpoints to manage security types in the system. Security types represent different classifications of financial instruments (e.g., common stock, preferred stock, corporate bonds, etc.).

### Endpoints

#### Get All Security Types
```http
GET /securityType
```
Returns a list of all security types.

**Response**
- `200 OK` - Returns an array of security types
```json
[
    {
        "securityTypeId": 1,
        "abbreviation": "CS",
        "description": "Common Stock",
        "versionId": 1
    }
]
```

#### Get Security Type by ID
```http
GET /securityType/{securityTypeId}
```
Returns a specific security type by ID.

**Response**
- `200 OK` - Returns the security type
- `404 Not Found` - If security type doesn't exist
```json
{
    "securityTypeId": 1,
    "abbreviation": "CS",
    "description": "Common Stock",
    "versionId": 1
}
```

#### Create Security Type
```http
POST /securityType
```
Creates a new security type.

**Request Body**
```json
{
    "securityTypeId": 1,
    "abbreviation": "CS",
    "description": "Common Stock",
    "versionId": 1
}
```

**Response**
- `201 Created` - Returns the created security type

#### Update Security Type
```http
PUT /securityType/{securityTypeId}
```
Updates an existing security type.

**Request Body**
```json
{
    "abbreviation": "CS",
    "description": "Common Stock",
    "versionId": 1
}
```

**Response**
- `200 OK` - Returns the updated security type
- `404 Not Found` - If security type doesn't exist
- `409 Conflict` - If version mismatch (optimistic locking)

#### Delete Security Type
```http
DELETE /securityType/{securityTypeId}?versionId={versionId}
```
Deletes a security type. Requires the current version for optimistic locking.

**Response**
- `204 No Content` - Successfully deleted
- `404 Not Found` - If security type doesn't exist
- `409 Conflict` - If version mismatch (optimistic locking)

### Data Model

| Field | Type | Description | Constraints |
|-------|------|-------------|------------|
| securityTypeId | Integer | Unique identifier | Required |
| abbreviation | String | Short code for the security type | Required, max 10 chars |
| description | String | Full description | Required, max 100 chars |
| versionId | Integer | Version for optimistic locking | Required |

### Error Handling

The API uses standard HTTP status codes and includes descriptive error messages:

- `404 Not Found` - Resource not found
- `409 Conflict` - Optimistic locking failure
- `400 Bad Request` - Invalid input
- `500 Internal Server Error` - Server-side errors

---

## Blotter API

The Blotter API provides endpoints to manage blotters, which are used to organize orders being worked by the trading desk.

### Endpoints

#### Get All Blotters
```http
GET /blotter
```
Returns a list of all blotters.

**Response**
- `200 OK` - Returns an array of blotters
```json
[
    {
        "blotterId": 1,
        "name": "Default",
        "autoPopulate": false,
        "securityTypeId": null,
        "versionId": 1
    }
]
```

#### Get Blotter by ID
```http
GET /blotter/{blotterId}
```
Returns a specific blotter by ID.

**Response**
- `200 OK` - Returns the blotter
- `404 Not Found` - If blotter doesn't exist
```json
{
    "blotterId": 1,
    "name": "Default",
    "autoPopulate": false,
    "securityTypeId": null,
    "versionId": 1
}
```

#### Create Blotter
```http
POST /blotter
```
Creates a new blotter.

**Request Body**
```json
{
    "name": "Default",
    "autoPopulate": false,
    "securityTypeId": null,
    "versionId": 1
}
```

**Response**
- `201 Created` - Returns the created blotter

#### Update Blotter
```http
PUT /blotter/{blotterId}
```
Updates an existing blotter.

**Request Body**
```json
{
    "name": "Default",
    "autoPopulate": false,
    "securityTypeId": null,
    "versionId": 1
}
```

**Response**
- `200 OK` - Returns the updated blotter
- `404 Not Found` - If blotter doesn't exist
- `409 Conflict` - If version mismatch (optimistic locking)

#### Delete Blotter
```http
DELETE /blotter/{blotterId}?versionId={versionId}
```
Deletes a blotter. Requires the current version for optimistic locking.

**Response**
- `204 No Content` - Successfully deleted
- `404 Not Found` - If blotter doesn't exist
- `409 Conflict` - If version mismatch (optimistic locking)

### Data Model

| Field | Type | Description | Constraints |
|-------|------|-------------|------------|
| blotterId | Integer | Unique identifier | Required |
| name | String | Display name of the blotter | Required, max 60 chars |
| autoPopulate | Boolean | If true, auto-populate orders with matching securityTypeId | Not required, defaults to false |
| securityTypeId | Integer | Used for auto-population | Not required |
| versionId | Integer | Version for optimistic locking | Required |

### Error Handling

The API uses standard HTTP status codes and includes descriptive error messages:

- `404 Not Found` - Resource not found
- `409 Conflict` - Optimistic locking failure
- `400 Bad Request` - Invalid input
- `500 Internal Server Error` - Server-side errors

---

## Trade Type API

The Trade Type API provides endpoints to manage trade types, which categorize the way a trade may be placed with the sell side (e.g., buy, sell, open, close, etc.).

### Endpoints

#### Get All Trade Types
```http
GET /tradeType
```
Returns a list of all trade types.

**Response**
- `200 OK` - Returns an array of trade types
```json
[
    {
        "tradeTypeId": 1,
        "abbreviation": "buy",
        "description": "buy",
        "versionId": 1
    }
]
```

#### Get Trade Type by ID
```http
GET /tradeType/{tradeTypeId}
```
Returns a specific trade type by ID.

**Response**
- `200 OK` - Returns the trade type
- `404 Not Found` - If trade type doesn't exist
```json
{
    "tradeTypeId": 1,
    "abbreviation": "buy",
    "description": "buy",
    "versionId": 1
}
```

#### Create Trade Type
```http
POST /tradeType
```
Creates a new trade type.

**Request Body**
```json
{
    "tradeTypeId": 1,
    "abbreviation": "buy",
    "description": "buy",
    "versionId": 1
}
```

**Response**
- `201 Created` - Returns the created trade type

#### Update Trade Type
```http
PUT /tradeType/{tradeTypeId}
```
Updates an existing trade type.

**Request Body**
```json
{
    "abbreviation": "buy",
    "description": "buy",
    "versionId": 1
}
```

**Response**
- `200 OK` - Returns the updated trade type
- `404 Not Found` - If trade type doesn't exist
- `409 Conflict` - If version mismatch (optimistic locking)

#### Delete Trade Type
```http
DELETE /tradeType/{tradeTypeId}?versionId={versionId}
```
Deletes a trade type. Requires the current version for optimistic locking.

**Response**
- `204 No Content` - Successfully deleted
- `404 Not Found` - If trade type doesn't exist
- `409 Conflict` - If version mismatch (optimistic locking)

### Data Model

| Field | Type | Description | Constraints |
|-------|------|-------------|------------|
| tradeTypeId | Integer | Unique identifier | Required |
| abbreviation | String | Abbreviation for the trade type | Required, max 10 chars |
| description | String | Long description of the trade type | Required, max 100 chars |
| versionId | Integer | Version for optimistic locking | Required |

### Error Handling

The API uses standard HTTP status codes and includes descriptive error messages:

- `404 Not Found` - Resource not found
- `409 Conflict` - Optimistic locking failure
- `400 Bad Request` - Invalid input
- `500 Internal Server Error` - Server-side errors
