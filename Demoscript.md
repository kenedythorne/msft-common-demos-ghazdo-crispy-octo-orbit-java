This repository has been setup for Microsoft personnel to play with a demo GHAzDO environment. 

## Resources ##
 - [GHAzDO pitch deck](https://microsoft.sharepoint.com/teams/GithubSales/_layouts/15/search.aspx/siteall?q=GHAZDO%20Pitch%20Deck) on [ghsales](aka.ms/ghsales)
 - [Sample delivery](https://aka.ms/ghazdo-sample-demo). This can be shared directly with customers.
 - [GHAzDO intro demo video](https://www.youtube.com/watch?v=cTkUhKkMD_c), covering why a tool like this is needed and how to set it up. This video is usually hidden behind a form to collect leads so do not spread it to far.
 - [ADO roadmap](https://aka.ms/azdo-roadmap)
 - [PR Gating Script](https://gh.io/GHAzDO_pr_gating). PR Gating is added to this repo via scripts and configurations. The product team plans to add this feature at a later state, the current setup is a temporary workaround. 
 - [GHAzDO documentation](https://learn.microsoft.com/en-us/azure/devops/repos/security/configure-github-advanced-security-features).

## Preparations ##

Please review the GHAzDO pitch deck and the intro demo video. The pitch deck is useful if you need to explain the why of Advanced Security. The demo is a good next step after the customer understands the need. 
Advanced customers might want to dig deeper into the GHAzDO security capabilities. For larger customers, you will get support from the GitHub account team in these discussions if you reach out. 

## Demo for the customer ##

Start by talking about how easy it is to enable GHAzDO and how you can enable it at the repo, project, or organization level. This is a competitive advantage, most of our competitors have clunky setups and extra installs for this. Start by opening the project settings and show where you can enable all for the project and also the checkbox to turn GHAzDO on automatically for new repos. With the latest updates, you can now enable secret scanning and code protection separately, giving you more granular control over which security features to activate. Next, go back to the repo list and show the icon showing GHAzDO is enabled. You can also show the repo enablement page and demonstrate how you can toggle between different scanning options. You can mention the confirmation dialog box that displays the number of new unique active committers. This is a good time to go into the licensing model for GHAzDO to explain how we bill per committer on a pro-rated, monthly basis to your linked Azure subscription. 

Talk about what happens when GHAzDO secret protection is enabled. How we will 'start a background process' to scan for forgotten secrets in the codebase. Leaked credentials are one of the main attack vectors and most of our competitors does not do it. 

Show Push Protection. Create a new ADO PAT that can be used for showing off push protection
[Setup a new ADO PAT](https://learn.microsoft.com/en-us/azure/devops/organizations/accounts/use-personal-access-tokens-to-authenticate?view=azure-devops&tabs=Windows#create-a-pat). 
Give it minimal access rights. Copy the key value. I do this in a new tab to make it easy to revoke later. Try to add the ADO PAT to any file. I normally use a markdown file so we don’t break anything in the code. It also lets you mention that our scanning works on any type of file and isn’t dependent on a label like ADO_PAT=x. Try and commit and show the push protection kick in. Mention that inside an IDE this same message would show up in your terminal instead. Show how you can change just 1 char of the PAT, and the push will not be blocked. This shows how we can keep our false positive rate low. Lastly, add skip-secret-scanning:true to your commit message to push the real secret into your code so we can work through the remediation process. Mention this is a break-glass mechanism in the rare case of a false positive or if you need to rotate an existing hard-coded secret. We never want to block a dev from getting their jobs done.

Mention while we bypass the push protection, we will still generate a finding. Switch over to the Advanced Security UI and show the secret finding we just pushed. Open up the finding and talk through how we show the secret and a recommended remediation path. Discuss how we now are able to verify whether secrets are active by communicating with some of our secret scanning partners. 
Swap back to your secret tab and revoke the PAT. Go back to the finding and re-verify that the secret is no longer active. Then use the close alert button. Mention the other options can be used if you want to sign off on the risk. Be sure to point out you can control access to who can sign-off on risk through standard ADO RBAC. By default, contributors can see alerts, project administrators can dismiss alerts, and project collection administrators can turn on/off GHAzDO. Go back to all findings and show that we preserve the history under closed alerts. Show that we have findings from our background/historic scan as well. Mention that we recently expanded support for non-provider patterns (JWT, certs, db connection strings). In the future we’ll also enable AI-based secret scanning for custom secrets. This is already available in the demo environment as part of early access beta testing.

Transition to talking about dependency and code scanning. We now support automatic dependency scanning with one-click enablement. This is currently in public preview. This will automatically "inject" the dependency scanning step into any pipeline targetting your default branch. This is the easiest way to get started with dependency scanning. We're also working towards a preview for CodeQL as well. However, for this demo, we will show the more traditional way of adding dependency and code scanning to a build pipeline.  Show the GHAS pipeline and note the CodeQL and dependency scanning tasks. Walkthrough the options of the initialize step and explain the different types of query packs, need to install CodeQL on self-hosted agents, etc…

Show in the GHAS pipeline build logs the result of a dependency scan. Show the number of direct and transient dependencies. We will check all these libraries against the GitHub advisory database. Any libraries used with known vulnerabilities will be flagged and show in the GHAzDO UI. Show the dependency alerts. Mention how you can sort/filter for example to look at just critical vulnerabilities. Dig into one vulnerability. Talk about the details and show the link out to the GitHub Advisory Database. Mention that soon we will have a deeper integration with ADO with dependabot where PRs will get created automatically to update vulnerable libraries.

Switch to code scanning. Talk about how CodeQL is a two-step process, of first building a Database describing the dataflow of the application and that we then run queries against this database. Show the results of the code scanning. Dig into one vulnerability and show the details. Jump into the code that is referenced, I usually pick a SQL injection. Show the code and explain the issue, mention that we know in this instance that the SQL query is user controlled, that is, the tool has detected that some parts of the SQL query comes unfiltered from user input. Again, since we can follow the data from source to sink we can keep our false positive rate low. 

PR gating come up in most discussions. That is, the ability to block new code going into the main branch if it adds any new vulnerabilities. If this come up, show the PR that is there and blocked. Show how a branch policy has been set on the main branch. Show the result of the PR - how it has been annotated with information about the vulnerabilities. Jump into the details referenced in the PR annotation and show how the scan has happened on the PR branch. Currently, this is done through a [custom script]( (https://gh.io/GHAzDO_pr_gating) but will soon be available as part of GHAzDO itself.

Discuss our new Security Overview page (org settings -> security -> security overview). This helps aggregate results beyond just the repo level. Note that this is just our first pass and there are plans to continue to expand these pages to bring more parity with what is available within GitHub. For customers looking for more information you can disuss the integration with Microsoft Defender. If you have access to Contoso Hotels, you should be able to show the basic experience within the Azure portal. 

For users of other security tools or for those with languages not supported in CodeQL like Terraform you can mention how we can import in findings from other tools and bring them into GHAzDO. The GHAS pipeline already has this in place with a prebuilt task in the marketplace for Trivy. If you show the code scanning findings you can show you can select a filter for tool=trivy. You can show a sample alert and mention the description here will come from the 3rd party vendor, not MSFT.

For advanced users interested in CodeQL you can mention that GHAzDO now supports [custom CodeQL queries](https://learn.microsoft.com/en-us/azure/devops/repos/security/github-advanced-security-code-scanning?view=azure-devops#using-custom-queries-with-codeql). This means you can define specific queries and issues to be scanned for in your projects. In this project, there is a pipeline named ‘GHASConfig’ that you can run manually if you want to mention this capability. This pipeline uses a configuration that excludes two of the standard queries (for demonstration purposes) and adds a custom query called ‘CallsToStringToString’. When you run the pipeline, you’ll see two different options in the dropdown on the code scanning results page. The results from the GHASConfig pipeline will exclude two standard alerts (see the config file for details) and include additional results related to unnecessary toString calls.

## Recent GHAzDO Releases ##

Stay informed about the latest GHAzDO improvements and features with these recent sprint updates:

### Sept 25, 2025 - Sprint 262 Update ###
[Sprint 262 Release Notes](https://learn.microsoft.com/azure/devops/release-notes/2025/ghazdo/sprint-262-update)

- New state filter available for Coverage in security overview to see what features are enabled across your organization

- Enhanced filtering by project and added search capabilities for Coverage

- Granular enablement panels now available for project and organization-level enablement


### Sept 4, 2025 - Sprint 261 Update ###
[Sprint 261 Release Notes](https://learn.microsoft.com/azure/devops/release-notes/2025/ghazdo/sprint-261-update)

- New SARIF processing completed service hook event for better integration with third-party tools

### Aug 11, 2025 - Sprint 260 Update ###
[Sprint 260 Release Notes](https://learn.microsoft.com/azure/devops/release-notes/2025/ghazdo/sprint-260-update)

- New secret scanning patterns for improved detection of sensitive information. See [full list](https://learn.microsoft.com/azure/devops/repos/security/github-advanced-security-secret-scan-patterns?view=azure-devops#partner-provider-patterns).

- Periodic repository re-scans performed for secret scanning to scan for newly introduced secret patterns in existing code.

- Alert dismissal reason now visible in the UI to improve auditability of dismissed alerts.

- Secret validity checking goes GA. This feature checks if a detected secret is still valid by querying the provider's API, helping to reduce false positives.

### July 17, 2025 - Sprint 259 Update ###
[Sprint 259 Release Notes](https://learn.microsoft.com/azure/devops/release-notes/2025/ghazdo/sprint-259-update)

- Advanced Security properly picks up on default branch changes and deleted branches

### June 30, 2025 - Sprint 258 Update ###
[Sprint 258 Release Notes](https://learn.microsoft.com/azure/devops/release-notes/2025/ghazdo/sprint-258-update)

- One click enablement for dependency scanning now in public preview

- Service hooks for GitHub Advanced Security for Azure DevOps alerts generally available  

### June 16, 2025 - Sprint 257 Update ###
[Sprint 257 Release Notes](https://learn.microsoft.com/azure/devops/release-notes/2025/ghazdo/sprint-257-update)

- GitHub Advanced Security is now available as GitHub Secret Protection and Code Security for Azure DevOps