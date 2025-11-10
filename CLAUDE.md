# Claude Assistant Guidelines

This file contains guidelines and context for Claude when working on this repository.

## Project Overview

This is a YouTube Clone application built with Kotlin Multiplatform (KMP) supporting:
- Android (including Android TV with TubeArchivist integration)
- iOS
- Desktop
- Web

## Technology Stack

- **Framework**: Compose Multiplatform
- **Language**: Kotlin
- **Build System**: Gradle
- **Platforms**: Android, iOS, Desktop, Web

## Code Style Guidelines

### Kotlin Code
- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Prefer immutability (val over var)
- Use extension functions where appropriate
- Add KDoc comments for public APIs

### Compose Multiplatform
- Keep composables small and focused
- Use state hoisting appropriately
- Follow Material Design guidelines
- Ensure UI components are platform-agnostic where possible

## Review Criteria

When reviewing code changes, please focus on:

1. **Functionality**: Does the code work as intended?
2. **Code Quality**: Is the code clean, readable, and maintainable?
3. **Performance**: Are there any potential performance issues?
4. **Security**: Are there any security vulnerabilities?
5. **Testing**: Are there appropriate tests?
6. **Multiplatform Compatibility**: Does the code work across all supported platforms?

## Git Workflow

- Branch naming: `claude/*` branches are used for Claude-generated code
- Commit messages should be clear and descriptive
- Auto-build APKs are triggered for all `claude/*` branches
- Rolling releases are created automatically for development builds

## Special Considerations

### Android TV / TubeArchivist Integration
- Located in `docs/android-tv-tubearchivist/`
- Follows architectural patterns defined in the implementation plan
- Uses AndroidX Leanback library for TV UI

### Build Process
- Debug APKs are built automatically via GitHub Actions
- APKs are uploaded as artifacts and as GitHub releases
- Build size and performance are important considerations

## Preferred Patterns

### State Management
- Use ViewModels for UI state
- Prefer unidirectional data flow
- Use Kotlin coroutines for asynchronous operations

### Dependency Injection
- Document any DI framework being used (if applicable)
- Keep dependencies minimal and well-organized

### Error Handling
- Use Result types or sealed classes for error handling
- Provide meaningful error messages
- Handle edge cases appropriately

## Testing Guidelines

- Write unit tests for business logic
- Write UI tests for critical user flows
- Ensure tests are platform-agnostic where possible
- Mock external dependencies appropriately

## Documentation

- Keep README.md updated with project changes
- Document architectural decisions
- Add inline comments for complex logic
- Update this CLAUDE.md file as project evolves

## Questions to Ask Before Implementation

1. Does this change affect multiple platforms?
2. Are there existing patterns in the codebase I should follow?
3. Will this change require updates to documentation?
4. Are there potential breaking changes?
5. Does this align with the project's architecture?

## Contact & Resources

- Repository: https://github.com/vishalkdotcom/Youtube-Clone
- Issues: Report bugs and feature requests via GitHub Issues
- Discussions: Use GitHub Discussions for questions and ideas
