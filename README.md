# Teneo Solution for Tiva

This project represents all of the resources used by the Tiva Teneo Studio solution. The solution file is available for each release here: [https://github.com/artificialsolutions-tiva/tiva-solution/releases](https://github.com/artificialsolutions-tiva/tiva-solution/releases) 

##Prerequisites
A 5.1.1 or greater studio environment.

##Project Structure
* external_sheets

>Contains a single file which is an excel sheet with the same structure as the google sheet passed into the solution.

* global_scripts

>Contains each of the scripts under Globals > Scripts in Teneo studio.

* resource_files

>Contains utility classes contained under Resources > File in Teneo studio.
>* BotFrameworkHelper: Supports Teams channel from [Teneo.ai](https://www.teneo.ai/engine/channels/microsoft-bot-framework/botframeworkhelper)
>* CosineSimilarity: Supports both context and FAQ Scraper, from [Teneo.ai](https://www.teneo.ai/studio/extensions/cosine-similarity)
>* ExtensionHelper: Supports Leopard channel from [Leopard](https://github.com/artificialsolutions-tiva/tiva-leopard-ui/blob/master/src/teneo-assets/ExtensionHelper.groovy)
>* GoogleSheetConnector: Supports pulling information from Google Sheet using Jaguar service.
>* KnowledgeHelper: Retrieves and parses all sheet information plus methods to support Tiva Flows.
>* UIHelper: Used to control formatting of answer text and output parameters per channel. Current Channels:
>    * webview: Leopard
>    * botframework-msteams: Microsoft Teams
>    * twilio-sms: Twilio SMS

* resources_jar

>Contains library files used by the solution that aren't available via link to other project.

* studio_integrations

>Contains a script representation of the Studio integrations under Resources > Integration in Teneo studio.

* testing_scripts 

>Contains scripts used to test classes from resource_files before added to solution to test new behaviors.
