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
    /// Deletes client user for the given buyer, client, and user IDs.
    /// </summary>
    public class DeleteClientUsers : ExampleBase
    {
        private AuthorizedBuyersMarketplaceService mkService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public DeleteClientUsers()
        {
            mkService = Utilities.GetAuthorizedBuyersMarketplaceService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example deletes a specific client user for a given client.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {"account_id", "client_id", "user_id"};
            bool showHelp = false;

            string accountId = null;
            string clientId = null;
            string userId = null;


            OptionSet options = new OptionSet {
                "Delete a client user with the given buyer, client, and user IDs.",
                {
                    "h|help",
                    "Show help message and exit.",
                    h => showHelp = h != null
                },
                {
                    "a|account_id=",
                    ("[Required] The resource ID of the buyers resource under which the parent " +
                     "client was created. This will be used to construct the name used as a path " +
                     "parameter for the users.delete request."),
                    a => accountId = a
                },
                {
                    "c|client_id=",
                    ("[Required] The resource ID of the buyers.clients resource for which the " +
                     "client user was created. This will be used to construct the name used as " +
                     "a path parameter for the users.delete request."),
                    c => clientId = c
                },
                {
                    "u|user_id=",
                    ("[Required] The resource ID of the buyers.clients.users resource for which " +
                     "the client user was created. This will be used to construct the name used " +
                     "as a path parameter for the users.delete request."),
                    u => userId = u
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
            // Set optional arguments.
            parsedArgs["account_id"] = accountId;
            parsedArgs["client_id"] = clientId;
            parsedArgs["user_id"] = userId;
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
            string userId = (string) parsedArgs["user_id"];
            string name = $"buyers/{accountId}/clients/{clientId}/users/{userId}";

            BuyersResource.ClientsResource.UsersResource.DeleteRequest request =
                mkService.Buyers.Clients.Users.Delete(name);

            Console.WriteLine("Deleting client user with name: {0}", name);

            try
            {
                request.Execute();
            }
            catch (Exception exception)
            {
                throw new ApplicationException(
                    $"Marketplace API returned error response:\n{exception.Message}");
            }
        }
    }
}
