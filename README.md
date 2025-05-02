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

---

## Destination API

The Destination API provides endpoints to manage destinations, which are exchanges or other executing entities where trades are sent.

### Endpoints

#### Get All Destinations
```http
GET /destination
```
Returns a list of all destinations.

**Response**
- `200 OK` - Returns an array of destinations
```json
[
    {
        "destinationId": 1,
        "abbreviation": "NYSE",
        "description": "New York Stock Exchange",
        "versionId": 1
    }
]
```

#### Get Destination by ID
```http
GET /destination/{destinationId}
```
Returns a specific destination by ID.

**Response**
- `200 OK` - Returns the destination
- `404 Not Found` - If destination doesn't exist
```json
{
    "destinationId": 1,
    "abbreviation": "NYSE",
    "description": "New York Stock Exchange",
    "versionId": 1
}
```

#### Create Destination
```http
POST /destination
```
Creates a new destination.

**Request Body**
```json
{
    "destinationId": 1,
    "abbreviation": "NYSE",
    "description": "New York Stock Exchange",
    "versionId": 1
}
```

**Response**
- `201 Created` - Returns the created destination

#### Update Destination
```http
PUT /destination/{destinationId}
```
Updates an existing destination.

**Request Body**
```json
{
    "abbreviation": "NYSE",
    "description": "New York Stock Exchange",
    "versionId": 1
}
```

**Response**
- `200 OK` - Returns the updated destination
- `404 Not Found` - If destination doesn't exist
- `409 Conflict` - If version mismatch (optimistic locking)

#### Delete Destination
```http
DELETE /destination/{destinationId}?versionId={versionId}
```
Deletes a destination. Requires the current version for optimistic locking.

**Response**
- `204 No Content` - Successfully deleted
- `404 Not Found` - If destination doesn't exist
- `409 Conflict` - If version mismatch (optimistic locking)

### Data Model

| Field | Type | Description | Constraints |
|-------|------|-------------|------------|
| destinationId | Integer | Unique identifier | Required |
| abbreviation | String | Abbreviation for the destination | Required, max 20 chars |
| description | String | Long description of the destination | Required, max 100 chars |
| versionId | Integer | Version for optimistic locking | Required |

### Error Handling

The API uses standard HTTP status codes and includes descriptive error messages:

- `404 Not Found` - Resource not found
- `409 Conflict` - Optimistic locking failure
- `400 Bad Request` - Invalid input
- `500 Internal Server Error` - Server-side errors

### Order Type API

The Order Type service provides endpoints to manage order types in the system.

#### Endpoints

##### Get All Order Types
- **GET** `/order-type`
- Returns a list of all order types
- Response: Array of order type objects
```json
[
    {
        "id": 1,
        "abbreviation": "MKT",
        "description": "Market Order",
        "version": 1
    }
]
```

##### Get Order Type by ID
- **GET** `/order-type/{orderTypeId}`
- Returns a specific order type by ID
- Response: Order type object or 404 if not found
```json
{
    "id": 1,
    "abbreviation": "MKT",
    "description": "Market Order",
    "version": 1
}
```

##### Create Order Type
- **POST** `/order-type`
- Creates a new order type
- Request body: Order type object (without ID)
```json
{
    "abbreviation": "LMT",
    "description": "Limit Order"
}
```
- Response: Created order type object with ID

##### Update Order Type
- **PUT** `/order-type/{orderTypeId}`
- Updates an existing order type
- Request body: Order type object
```json
{
    "abbreviation": "LMT",
    "description": "Limit Order",
    "version": 1
}
```
- Response: Updated order type object
- Status: 404 if not found, 409 if version mismatch

##### Delete Order Type
- **DELETE** `/order-type/{orderTypeId}?versionId={version}`
- Deletes an order type
- Parameters:
  - orderTypeId: ID of the order type to delete
  - versionId: Current version of the order type
- Status: 204 on success, 404 if not found, 409 if version mismatch

## Order Status API

The Order Status service provides endpoints to manage order statuses in the system. Order statuses define the current state of an order (e.g., new, open, blocked, sent, filled, cancelled).

### Endpoints

#### Get All Order Statuses
```http
GET /order-status
```
Returns a list of all order statuses.

**Response**
- `200 OK` - Returns an array of order statuses
```json
[
    {
        "id": 1,
        "abbreviation": "new",
        "description": "Newly created order",
        "version": 1
    }
]
```

#### Get Order Status by ID
```http
GET /order-status/{orderStatusId}
```
Returns a specific order status by ID.

**Response**
- `200 OK` - Returns the order status
- `404 Not Found` - If order status doesn't exist
```json
{
    "id": 1,
    "abbreviation": "new",
    "description": "Newly created order",
    "version": 1
}
```

#### Create Order Status
```http
POST /order-status
```
Creates a new order status.

**Request Body**
```json
{
    "abbreviation": "new",
    "description": "Newly created order"
}
```

**Response**
- `201 Created` - Returns the created order status

#### Update Order Status
```http
PUT /order-status/{orderStatusId}
```
Updates an existing order status.

**Request Body**
```json
{
    "abbreviation": "new",
    "description": "Newly created order",
    "version": 1
}
```

**Response**
- `200 OK` - Returns the updated order status
- `404 Not Found` - If order status doesn't exist
- `409 Conflict` - If version mismatch (optimistic locking)

#### Delete Order Status
```http
DELETE /order-status/{orderStatusId}?versionId={version}
```
Deletes an order status.

**Parameters**
- orderStatusId: ID of the order status to delete
- versionId: Current version of the order status

**Response**
- `204 No Content` - Successfully deleted
- `404 Not Found` - If order status doesn't exist
- `409 Conflict` - If version mismatch (optimistic locking)

### Data Model

| Field | Type | Description | Constraints |
|-------|------|-------------|------------|
| id | Integer | Unique identifier | Required |
| abbreviation | String | Short code for the order status | Required, max 20 chars |
| description | String | Full description | Required, max 60 chars |
| version | Integer | Version for optimistic locking | Required |

### Error Handling

The API uses standard HTTP status codes and includes descriptive error messages:

- `404 Not Found` - Resource not found
- `409 Conflict` - Optimistic locking failure
- `400 Bad Request` - Invalid input
- `500 Internal Server Error` - Server-side errors

## Security Service

The Security service provides CRUD operations for managing securities in the GlobeCo Trade Blotter system. A security represents an interest in equity, debt, or other rights.

### API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| GET | /security | Retrieves all securities |
| GET | /security/{securityId} | Retrieves a specific security by ID |
| POST | /security | Creates a new security |
| PUT | /security/{securityId} | Updates an existing security |
| DELETE | /security/{securityId} | Deletes a security (requires versionId parameter) |

### Data Model

| Field | Type | Description | Constraints |
|-------|------|-------------|------------|
| securityId | Integer | Immutable resource identifier | Required |
| ticker | String | Trading abbreviation for security | Required, max 50 chars |
| description | String | Description of the security | Max 200 chars |
| securityTypeId | Integer | The security type of the security | Required |
| versionId | Integer | Version field for concurrency management | Required |

### Sample Request/Response

#### Create Security
```json
POST /security
{
  "ticker": "IBM",
  "description": "IBM Corporation",
  "securityTypeId": 1
}
```

#### Response
```json
{
  "securityId": 1,
  "ticker": "IBM",
  "description": "IBM Corporation",
  "securityTypeId": 1,
  "versionId": 1
}
```

### Error Responses

| Status Code | Description |
|-------------|-------------|
| 404 | Security not found |
| 409 | Concurrent modification detected |

## Order Service

The Order service provides operations for managing trading orders in the GlobeCo Trade Blotter system. An order represents a trading instruction sent from a portfolio manager to a trader.

### API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| GET | /order | Retrieves all orders |
| GET | /order/{orderId} | Retrieves a specific order by ID |
| POST | /order | Creates a new order |
| PUT | /order/{orderId} | Updates an existing order |
| DELETE | /order/{orderId} | Deletes an order (requires versionId parameter) |
| POST | /order/{orderId}/blotter/{blotterId} | Updates the blotter assignment |
| POST | /order/{orderId}/status/{statusId} | Updates the order status |

### Data Model

| Field | Type | Description | Constraints |
|-------|------|-------------|-------------|
| orderId | Integer | Immutable resource identifier | Required |
| securityId | Integer | The identifier of the security to trade | Foreign key to security. Required |
| blotterId | Integer | The blotter on which this security will land | Foreign key to blotter |
| quantity | Numeric | The quantity of the order | Must be greater than 0. Maximum size is 9999999999.99999999 |
| orderTimestamp | String | The time the order is placed with time zone | Automatically populated with database time in ISO 8601 format |
| orderTypeId | Integer | The type of the order | Foreign key to orderType. Required |
| orderStatusId | Integer | The status of the order | Foreign key to orderStatus. Defaults to 1 if not supplied |
| versionId | Integer | Version field for concurrency management | Required |

### Business Rules

1. **Blotter Assignment Rule**: On order creation, if no blotter is specified and there exists a blotter with:
   - autoPopulate = true
   - securityType matching the order's security's type
   Then that blotter is automatically assigned to the order.

2. **Order Status**: 
   - New orders default to status 1 (new) if not specified
   - Orders can only be deleted when in status 1 (new)

3. **Timestamps**:
   - orderTimestamp is automatically set on creation
   - orderTimestamp cannot be modified by clients

4. **Concurrency Control**:
   - All modifications require the current version number
   - Optimistic locking is enforced using the version field

### Error Responses

| Status Code | Description |
|-------------|-------------|
| 404 | Resource not found |
| 409 | Concurrent modification conflict |
| 400 | Invalid request (e.g., deleting non-new order) |

---

## Block API

The Block API provides endpoints to manage blocks, which are groups of orders for the same security and order type placed around the same time.

### Endpoints

#### Get All Blocks
```http
GET /block
```
Returns a list of all blocks.

**Response**
- `200 OK` - Returns an array of blocks

#### Get Block by ID
```http
GET /block/{blockId}
```
Returns a specific block by ID.

**Response**
- `200 OK` - Returns the block
- `404 Not Found` - If block doesn't exist

#### Create Block
```http
POST /block
```
Creates a new block.

**Request Body**
```json
{
    "security": { ... },
    "orderType": { ... },
    "version": 1
}
```

**Response**
- `201 Created` - Returns the created block

#### Update Block
```http
PUT /block/{blockId}
```
Updates an existing block.

**Request Body**
```json
{
    "security": { ... },
    "orderType": { ... },
    "version": 1
}
```

**Response**
- `200 OK` - Returns the updated block
- `404 Not Found` - If block doesn't exist
- `409 Conflict` - If version mismatch (optimistic locking)

#### Delete Block
```http
DELETE /block/{blockId}?versionId={version}
```
Deletes a block. Requires the current version for optimistic locking.

**Response**
- `204 No Content` - Successfully deleted
- `404 Not Found` - If block doesn't exist
- `409 Conflict` - If version mismatch (optimistic locking)

### Data Model

| Field | Type | Description | Constraints |
|-------|------|-------------|------------|
| blockId | Integer | Unique identifier | Required |
| security | Security | The security for the block | Required |
| orderType | OrderType | The order type of the block | Required |
| version | Integer | Version for optimistic locking | Required |

### Error Handling

The API uses standard HTTP status codes and includes descriptive error messages:

- `404 Not Found` - Resource not found
- `409 Conflict` - Optimistic locking failure
- `400 Bad Request` - Invalid input
- `500 Internal Server Error` - Server-side errors
