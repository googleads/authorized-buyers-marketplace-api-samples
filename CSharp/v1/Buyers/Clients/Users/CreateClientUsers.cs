/* Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

using Google.Apis.AuthorizedBuyersMarketplace.v1;
using Google.Apis.AuthorizedBuyersMarketplace.v1.Data;
using Mono.Options;

using System;
using System.Collections.Generic;

namespace Google.Apis.AuthorizedBuyersMarketplace.Examples.v1.Buyers.Clients.Users
{
    /// <summary>
    /// Creates a client user for the given client.
    ///
    /// When a client user is created, the specified email address will receive an email to confirm
    /// access to the Authorized Buyers UI. It will remain in the "INVITED" state and be unable to
    /// access the UI until the specified email has approved of the change.
    /// </summary>
    public class CreateClientUsers : ExampleBase
    {
        private AuthorizedBuyersMarketplaceService mkService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public CreateClientUsers()
        {
            mkService = Utilities.GetAuthorizedBuyersMarketplaceService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example creates a client user for the given client.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {"account_id", "client_id"};
            bool showHelp = false;

            string accountId = null;
            string clientId = null;
            string email = null;

            OptionSet options = new OptionSet {
                "Creates a client user for the given client.",
                {
                    "h|help",
                    "Show help message and exit.",
                    h => showHelp = h != null
                },
                {
                    "a|account_id=",
                    ("[Required] The resource ID of the buyers resource under which the " +
                     "client was created. This will be used to construct the name used as a " +
                     "path parameter for the users.create request."),
                    a => accountId = a
                },
                {
                    "c|client_id=",
                    ("[Required] The resource ID of the clients resource under which the client " +
                     "user is to be created. This will be used to construct the name used as a " +
                     "path parameter for the users.create request."),
                    c => clientId = c
                },
                {
                    "e|email=",
                    ("The client user's email address that has to be unique across all client " +
                     "users for a given client. By default, this will be set to a randomly " +
                     "generated email for demonstration purposes."),
                    e => email = e
                },
            };

            List<string> extras = options.Parse(exampleArgs);
            var parsedArgs = new Dictionary<string, object>();

            // Show help message.
            if (showHelp == true)
            {
                options.WriteOptionDescriptions(Console.Out);
                Environment.Exit(0);
            }
            // Set arguments.
            parsedArgs["account_id"] = accountId;
            parsedArgs["client_id"] = clientId;

            Random rng = new Random();

            parsedArgs["email"] = email ?? String.Format(
                "testemail{0}@test.com",
                rng.Next(10000000, 99999999));

            // Validate that options were set correctly.
            Utilities.ValidateOptions(options, parsedArgs, requiredOptions, extras);

            return parsedArgs;
        }

        /// <summary>
        /// Run the example.
        /// </summary>
        /// <param name="parsedArgs">Parsed arguments for the example.</param>
        protected override void Run(Dictionary<string, object> parsedArgs)
        {
            string accountId = (string) parsedArgs["account_id"];
            string clientId = (string) parsedArgs["client_id"];
            string parent = $"buyers/{accountId}/clients/{clientId}";

            ClientUser newClientUser = new ClientUser()
            {
                Email = (string) parsedArgs["email"]
            };

            BuyersResource.ClientsResource.UsersResource.CreateRequest request =
                mkService.Buyers.Clients.Users.Create(newClientUser, parent);
            ClientUser response = null;

            Console.WriteLine("Creating client user for client: {0}", parent);

            try
            {
                response = request.Execute();
            }
            catch (Exception exception)
            {
                throw new ApplicationException(
                    $"Real-time Bidding API returned error response:\n{exception.Message}");
            }

            Utilities.PrintClientUser(response);
        }
    }
}
