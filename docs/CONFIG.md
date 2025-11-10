# Configuration Guide

## TubeArchivist API Configuration

### API Credentials

**Base URL**: `https://ta.vishalk.com`
**API Token**: `ddb865bf6f8970f8b52283a09f939316eb17c66d`

### Authentication

All API requests require the following header:
```
Authorization: Token ddb865bf6f8970f8b52283a09f939316eb17c66d
```

### Security Best Practices

⚠️ **IMPORTANT**: The API token in this document is for development purposes. For production:

1. **Never commit tokens to git**
   - Add to `.gitignore`
   - Use BuildConfig or environment variables

2. **Use BuildConfig for Android**
   ```kotlin
   buildConfig {
       buildConfigField("String", "TUBE_ARCHIVIST_URL", "\"https://ta.vishalk.com\"")
       buildConfigField("String", "TUBE_ARCHIVIST_TOKEN", "\"your_token_here\"")
   }
   ```

3. **Store securely on device**
   - Use EncryptedSharedPreferences for API tokens
   - Never log tokens in production builds

### Network Security Configuration

For Android TV, add this to your `network_security_config.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="false">
        <domain includeSubdomains="true">ta.vishalk.com</domain>
    </domain-config>
</network-security-config>
```

**Note**: Set `cleartextTrafficPermitted="true"` only if ta.vishalk.com uses HTTP instead of HTTPS.

### API Endpoints

Based on TubeArchivist documentation:

#### Videos
- `GET /api/video/` - List all videos (paginated)
- `GET /api/video/{video_id}/` - Get video details
- `POST /api/video/{video_id}/progress` - Update watch progress

#### Channels
- `GET /api/channel/` - List all channels (paginated)
- `GET /api/channel/{channel_id}/` - Get channel details
- `GET /api/channel/{channel_id}/video` - Get channel videos

#### Search
- `GET /api/search/` - Search videos, channels, playlists
  - Query params: `query`, `page`

#### Playlists
- `GET /api/playlist/` - List all playlists
- `GET /api/playlist/{playlist_id}/` - Get playlist details

#### Authentication
- `POST /api/login` - Login with username/password to get token

### Media URLs

#### Video Streaming
```
https://ta.vishalk.com/media/{channel_id}/{video_id}.mp4
```

#### Thumbnails
Video thumbnail:
```
https://ta.vishalk.com/cache/videos/{video_id}.jpg
```

Channel thumbnail:
```
https://ta.vishalk.com/cache/channels/{channel_id}_thumb.jpg
```

Channel banner:
```
https://ta.vishalk.com/cache/channels/{channel_id}_banner.jpg
```

### Pagination

TubeArchivist uses standard pagination:

```json
{
  "paginate": {
    "page_size": 24,
    "current_page": 1,
    "total_hits": 150,
    "last_page": 7
  },
  "data": [...]
}
```

Query parameter: `?page=1` (defaults to 1)

### Example Request with Ktor

```kotlin
val client = HttpClient(OkHttp) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
        })
    }
    defaultRequest {
        header("Authorization", "Token ddb865bf6f8970f8b52283a09f939316eb17c66d")
        header("Content-Type", "application/json")
    }
}

val response: VideoResponse = client.get("https://ta.vishalk.com/api/video/") {
    parameter("page", 1)
}.body()
```

### Testing API Access

Use curl to test API connectivity:

```bash
curl -H "Authorization: Token ddb865bf6f8970f8b52283a09f939316eb17c66d" \
     https://ta.vishalk.com/api/video/
```

### Troubleshooting

**Issue**: 403 Forbidden
- **Solution**: Check API token is correct
- **Solution**: Ensure token is included in Authorization header

**Issue**: Connection refused
- **Solution**: Verify ta.vishalk.com is accessible
- **Solution**: Check network security config allows the domain

**Issue**: SSL errors
- **Solution**: Ensure domain has valid SSL certificate
- **Solution**: Update network security config if using self-signed cert

**Issue**: 404 Not Found
- **Solution**: Verify API endpoint path is correct
- **Solution**: Check TubeArchivist version and API compatibility

### Rate Limiting

TubeArchivist doesn't document rate limits in the public docs. Implement:
- Exponential backoff for retries
- Request caching to minimize API calls
- Respect pagination (don't request all pages at once)

### API Documentation

Full API documentation available at:
- **Live Instance**: https://ta.vishalk.com/api/docs/
- **Official Docs**: https://docs.tubearchivist.com/api/

### Environment-Specific Configuration

#### Development
```kotlin
const val BASE_URL = "https://ta.vishalk.com"
const val LOG_LEVEL = LogLevel.ALL
const val TIMEOUT_MS = 30_000L
```

#### Production
```kotlin
const val BASE_URL = BuildConfig.TUBE_ARCHIVIST_URL
const val LOG_LEVEL = LogLevel.NONE
const val TIMEOUT_MS = 15_000L
```

---

## Build Configuration

### Gradle Properties

Add to `local.properties` (not committed to git):
```properties
tubeArchivistUrl=https://ta.vishalk.com
tubeArchivistToken=ddb865bf6f8970f8b52283a09f939316eb17c66d
```

### BuildConfig Usage

```kotlin
buildConfig {
    val tubeArchivistUrl: String by project
    val tubeArchivistToken: String by project

    buildConfigField("String", "TUBE_ARCHIVIST_URL", "\"$tubeArchivistUrl\"")
    buildConfigField("String", "TUBE_ARCHIVIST_TOKEN", "\"$tubeArchivistToken\"")
}
```

### Accessing in Code

```kotlin
val apiUrl = BuildConfig.TUBE_ARCHIVIST_URL
val apiToken = BuildConfig.TUBE_ARCHIVIST_TOKEN
```

---

## Additional Resources

- [TubeArchivist GitHub](https://github.com/tubearchivist/tubearchivist)
- [TubeArchivist Documentation](https://docs.tubearchivist.com/)
- [TubeArchivist API Docs](https://docs.tubearchivist.com/api/)
