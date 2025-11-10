# Claude GitHub Assistant Setup Guide

This guide will help you set up Claude AI integration with your GitHub repository.

## Overview

The Claude Assistant GitHub Action allows you to interact with Claude AI directly from GitHub issues and pull requests. Simply mention `@claude` in a comment to get assistance with:

- Code reviews
- Bug fixes
- Feature implementations
- Code explanations
- Architecture suggestions
- And more!

## Prerequisites

1. **GitHub Repository Admin Access**: You need admin rights to configure secrets
2. **Anthropic API Key**: Required to use Claude AI

## Setup Steps

### Step 1: Get Your Anthropic API Key

1. Visit [Anthropic Console](https://console.anthropic.com)
2. Sign in or create an account
3. Navigate to API Keys section
4. Generate a new API key
5. **Important**: Copy the key immediately as you won't be able to see it again

### Step 2: Add API Key to GitHub Secrets

1. Go to your GitHub repository
2. Click on **Settings** → **Secrets and variables** → **Actions**
3. Click **New repository secret**
4. Name: `ANTHROPIC_API_KEY`
5. Value: Paste your Anthropic API key
6. Click **Add secret**

### Step 3: Verify the Workflow

1. The workflow file is already in place: `.github/workflows/claude-assistant.yml`
2. Go to **Actions** tab in your repository
3. You should see "Claude Assistant" in the list of workflows

### Step 4: Test the Integration

1. Create a new issue or pull request
2. Add a comment mentioning `@claude` with a question or request
3. Example: `@claude can you review this code?`
4. Claude should respond within a few moments

## Usage Examples

### In Issues

```
@claude can you help implement user authentication for this app?
```

```
@claude what's the best approach to add offline support?
```

### In Pull Requests

```
@claude can you review this PR and suggest improvements?
```

```
@claude check for any security vulnerabilities in this code
```

### Code Reviews

```
@claude review the changes in src/main/kotlin/App.kt
```

## Configuration Options

The workflow supports several customization options:

### Custom Trigger Phrase

By default, `@claude` triggers the assistant. You can customize this by editing `.github/workflows/claude-assistant.yml`:

```yaml
trigger_phrase: "/claude"  # Use /claude instead of @claude
```

### Advanced Claude Arguments

You can customize Claude's behavior with additional arguments:

```yaml
claude_args: |
  --max-turns 10
  --model claude-4-0-sonnet-20250805
```

## Project Guidelines

The `CLAUDE.md` file in the repository root contains project-specific guidelines that Claude will follow when working on your code. You can customize this file to:

- Define coding standards
- Specify architectural patterns
- Set review criteria
- Document preferred practices

## Alternative Authentication Methods

Besides Anthropic's direct API, Claude Code Action also supports:

### AWS Bedrock

Use AWS credentials for Claude access. Requires additional configuration in the workflow file.

### Google Vertex AI

Use Google Cloud credentials. Requires OIDC authentication setup.

For detailed instructions on cloud provider setup, visit the [official documentation](https://code.claude.com/docs/en/github-actions).

## Troubleshooting

### Claude doesn't respond

1. **Check API Key**: Verify the `ANTHROPIC_API_KEY` secret is set correctly
2. **Check Workflow**: Go to Actions tab and check if the workflow ran
3. **Check Permissions**: Ensure the workflow has the required permissions
4. **Check Logs**: View workflow logs for error messages

### Workflow fails

1. Check the Actions tab for error details
2. Verify your API key is valid
3. Check if you have sufficient API credits
4. Review the workflow file syntax

### Rate Limits

If you hit rate limits:
- Consider adding delays between requests
- Use the `--max-turns` argument to limit conversation length
- Monitor your API usage in the Anthropic Console

## Cost Considerations

- Each Claude interaction consumes GitHub Actions minutes
- API calls are billed according to your Anthropic pricing plan
- Use specific `@claude` commands to reduce unnecessary API calls
- Consider setting up usage alerts in the Anthropic Console

## Security Best Practices

- **Never commit API keys** to your repository
- Always use GitHub Secrets for sensitive data
- Regularly rotate your API keys
- Monitor workflow logs for any suspicious activity
- Review Claude's changes before merging

## Additional Resources

- [Claude Code Documentation](https://code.claude.com/docs)
- [Anthropic API Documentation](https://docs.anthropic.com)
- [GitHub Actions Documentation](https://docs.github.com/en/actions)

## Support

If you encounter issues:
1. Check the [official documentation](https://code.claude.com/docs)
2. Review existing [GitHub issues](https://github.com/anthropics/claude-code-action/issues)
3. Create a new issue with detailed information about your problem

## Quick Start Command

You can also use Claude Code CLI to set up GitHub integration:

```bash
claude /install-github-app
```

This command will guide you through the setup process automatically.

---

**Ready to start?** Just add your `ANTHROPIC_API_KEY` to GitHub Secrets and mention `@claude` in any issue or PR comment!
