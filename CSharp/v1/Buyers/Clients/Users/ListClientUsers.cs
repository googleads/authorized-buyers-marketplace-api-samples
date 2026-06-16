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
    /// Lists client users for a given client.
    /// </summary>
    public class ListClientUsers : ExampleBase
    {
        private AuthorizedBuyersMarketplaceService mkService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public ListClientUsers()
        {
            mkService = Utilities.GetAuthorizedBuyersMarketplaceService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example lists all client users for a given client.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {"account_id", "client_id"};
            bool showHelp = false;

            string accountId = null;
            string clientId = null;
            int? pageSize = null;

            OptionSet options = new OptionSet {
                "List clients for the given buyer account.",
                {
                    "h|help",
                    "Show help message and exit.",
                    h => showHelp = h != null
                },
                {
                    "a|account_id=",
                    ("[Required] The resource ID of the buyers resource under which the " +
                     "clients were created. This will be used to construct the parent used as " +
                     "a path parameter for the users.list request."),
                    a => accountId = a
                },
                {
                    "c|client_id=",
                    ("[Required] The resource ID of the buyers.clients resource under which the " +
                     "users were created. This will be used to construct the name used as a " +
                     "path parameter for the users.list request."),
                    c => clientId = c
                },
                {
                    "p|page_size=",
                    ("The number of rows to return per page. The server may return fewer rows " +
                     "than specified."),
                    (int p) => pageSize =  p
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
            parsedArgs["pageSize"] = pageSize ?? Utilities.MAX_PAGE_SIZE;
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
            string pageToken = null;

            Console.WriteLine(@"Listing client users for client with name: ""{0}""", parent);
            do
            {
                BuyersResource.ClientsResource.UsersResource.ListRequest request =
                    mkService.Buyers.Clients.Users.List(parent);
                request.PageSize = (int) parsedArgs["pageSize"];
                request.PageToken = pageToken;

                ListClientUsersResponse page = null;

                try
                {
                    page = request.Execute();
                }
                catch (Exception exception)
                {
                    throw new ApplicationException(
                        $"Marketplace API returned error response:\n{exception.Message}");
                }

                var users = page.ClientUsers;
                pageToken = page.NextPageToken;

                if (users == null)
                {
                    Console.WriteLine("No client users found for the given client.");
                }
                else
                {
                    foreach (ClientUser user in users)
                    {
                        Utilities.PrintClientUser(user);
                    }
                }
            }
            while(pageToken != null);
        }
    }
}
