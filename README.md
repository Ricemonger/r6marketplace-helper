# Rainbow 6 Siege Marketplace Trade Manager

This is app, which collects data about all items on Marketplace and utilize it to User’s advantage, able to automatically create/update/cancel Trade 
Orders on User’s Ubisoft Account.  
Requires from end User only his Ubisoft Account’s credentials, specification of items to be managed and boundaries for manager, which can be 
 configured for each item separately.  
&nbsp;  
[Marketplace](https://www.ubisoft.com/en-us/game/rainbow-six/siege/marketplace?route=home)


## To run:  
 1. Provide required auth_constants.env file by its sample  
    &nbsp;  
 2. Provide empty or configured(auth_constants.env values have higher priority) auth-values.yaml files in **main_user_reauthorizer**, **users_ubi_accs_reauthorizer**, **notifications_service** and **telegram_bot_updates_consumer** modules' src/main/resources directory  
    &nbsp;  
 3. Provide **fetching_accounts_credentials.json** in **fetching_users_reauthorizer** module's src/main/resources with Uplay accounts' credentials(more the better)
    One accounts may be used to deal 200 request per minute(300ms), fast_sell_trades_manager's SCHEDULING_FIXED_DELAY_ONE_USER_FAST_SELL_MANAGEMENT_FETCH by amount of accounts  
     &nbsp;  
 4. Run the app with ```docker-compose up``` in root directory  
     &nbsp;
 5. fast_sell_trades_manager service in **docker-compose.yaml** must be launched separately with confugired FAST_SELL_USER_ID and FAST_SELL_EMAIL after registering user via Telegram Bot


## To use:

For using **Fast Sell Trade Manager**, after linking your Ubisoft Account via Telegram Bot,
configure environmental variables in **fast_sell_trade_manager** module's docker-compose service to select only one user for being managed.    
Can create/update/cancel only sell trades. Expression for trade priority is unchangeable, only boundary price difference from normal price for item can be configured.  
Profit is approximately **7000-10000** credits/day for account with 1600 items.  


For using **Trades Manager**, link your Ubisoft Account and create Trade Manager via Telegram Bot. Any amount of users can user this service.  
Can create/update/cancel both buy and sell orders on Marketplace. Buy and Sell trade priority expression can be configured by each user personally.  
Trade Managers can use either Items' Filters or single Item ID. User can have any amount of Trade Managers.  
  * For Managers by Item ID, you can provide boundary prices to be sold/bought and multiplier for trade priority, to highlight managed item among 
   items from other managers.  
   * For Managers by Item Filters, instead of boundary prices, you can provide minimum difference between trade price and item's month median price.
      Can use few Item Filters at once. Item Filters are also created by User.
     
Profit is approximately **1200** credits/day for account with 1600 items and ~250.000 credits account worth.


## Environmental authorization variables description:  

>**UBI_PLATFORM** - platform of your Ubisoft Account, typically UPLAY  
**UBI_EMAIL** - your Ubisoft Account's email  
**UBI_PASSWORD** - your Ubisoft Account's password  
&nbsp;  
Used in  **Main User Reauthorizer**.  
Accounts itself is used in **Configs Fetcher**, **Item Day Sales Ubi Stats Fetcher**, **Item Stats Fetcher**.   
It's better to not use it in **fetching_accounts_credentials.json**, to prevent too many requests error during stats fetching.  
&nbsp;  
[Create Ubisoft Account](https://account.ubisoft.com/en-US/login)


>**TELEGRAM_BOT_TOKEN** - your Telegram Bot token, receive from @BotFather  
**TELEGRAM_BOT_USERNAME** - your Telegram Bot username, receive from @BotFather  
&nbsp;  
Used in **Telegram Bot Updates Consumer** and **Notifications Service**.  
&nbsp;  
[@BotFather](https://t.me/BotFather)


>**PASSWORD_ENCRYPTION_KEY** - AES password encryption key for storing Users' Linked Ubisoft Accounts' passwords in DB 
&nbsp;  
Used in **Telegram Bot Updates Consumer** and **Users Ubisoft Accounts Reauthorizer**.  
&nbsp;  
[AES encryption key generator - get Encryption Key 256](https://acte.ltd/utils/randomkeygen)


>**POSTGRES_USER** - Postgres root user, use any  
**POSTGRES_PASSWORD** - Postgres root password, use any  
&nbsp;  
**PGADMIN_DEFAULT_EMAIL** - PgAdmin root user email, use any  
**PGADMIN_DEFAULT_PASSWORD** - PgAdmin root user password, use any  


## Modules Description:


* **Configs Fetcher** - fetches all available Item Tags and Types, Marketplace's limitations of Trade slots, per day Trades limits and Trade expire timeout.  
Uses Main Ubisoft Account's Authorization Ticket and Headers, received from Redis service.


* **Fast Sell Trade Manager** - manages Sell Trades only for 1 selected User from DB.  
Selects managed account by User's ID and linked Ubisoft Account's email, provided in configuration file.  
Uses this account's Authorization Ticket and Headers, received from DB.  
Also uses Fetching Users from DB to fetch the data more often, avoiding too many requests errors.


* **Fetching Users Reauthorizer** - reauthorizes Fetching Users, which are used in **Fast Sell Trade Manager**, and
saves their Authorization Tickets and Headers to DB.


* **Item Day Sales Ubi Stats Fetcher** - fetches and saves to DB all items last month sales stats, provided by Ubisoft (only Item's min/max/avg day 
prices and sales count, per each day).  
Uses Main Ubisoft Account's Authorization Ticket and Headers, received from Redis service.


* **Item Stats Fetcher** - fetches and saves to DB all Items' current stats, including their last sales, as more accurate sale stats, comparing to 
day sales stats, provided by Ubisoft. May notify all users via Notifications Service, if items' amount was increased (which means that Marketplace was updated).  
Uses Main Ubisoft Account's Authorization Token and Headers, received from Redis service.


* **Item Trade Stats Calculator** - recalculates Items' sale history stats in DB, such as month min/max/avg/median prices, approximate price to 
buy in chosen time or to time to sell by chosen price, trade priorities, bases on Item's sale stats. Saves recalculated stats to DB.


* **Main User Reauthorizer** - reauthorizes Main User, which is used in **Configs Fetcher**, **Item Day Sales Ubi Stats Fetcher**
and **Item Stats Fetcher** to fetch data from Ubisoft Server. Saves its Authorization Ticket and Headers to Redis service.   


* **Notifications Service** - sends notifications to User via Telegram Bot, consuming requests from Kafka service.  


* **Telegram Bot Updates Consumer** - the main and only User Interface. Provided via Telegram Bot.  
Allows User to:  
Fetch Items by Item Filters  
Operate Trade Managers  
Operate Item Filters  
Link Managed Ubisoft Account  
Configure Notifications  


* **Trades Manager** - manages trades for all Users in DB, who have linked Ubisoft Account, have trade managers and enabled trade management in settings.
Uses Users and Items stats from DB.  
Notifies Users via Notifications Service about performed operations.  


* **Ubi Users Stats Fetcher** - fetches stats for linked Ubisoft Accounts and saves them to DB. Notifies Ubisoft Account owners via Notifications 
Service about any changes.  


* **Users Ubisoft Accounts Reauthorizer** - reauthorizes linked Ubisoft Accounts and saves their Authorization Tickets and Headers to DB.
Notifies Users via Notifications Service if theirs Ubisoft Account's reauthorization failed.


* **Utils** - contains shared resources:  
utils-dependencies - parent pom.xml for all modules.  
utils-library - shared DTOs, Enums, Exceptions, Services, etc.  
utils-library-graphQl - everything related to GraphQL.  
utils-library-kafka - classes, related to Kafka producers.  
utils-library-redis - Redis service.  
utils-postgres-schema - JPA entities and id classes.


