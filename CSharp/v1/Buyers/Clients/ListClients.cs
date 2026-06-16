/* Copyright 2021 Google LLC
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

namespace Google.Apis.AuthorizedBuyersMarketplace.Examples.v1.Buyers.Clients
{
    /// <summary>
    /// Lists clients for a given buyer account ID.
    /// </summary>
    public class ListClients : ExampleBase
    {
        private AuthorizedBuyersMarketplaceService mkService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public ListClients()
        {
            mkService = Utilities.GetAuthorizedBuyersMarketplaceService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example lists all clients for a given buyer account.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {"account_id"};
            bool showHelp = false;

            string accountId = null;
            string filter = null;
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
                     "a path parameter for the clients.list request."),
                    a => accountId = a
                },
                {
                    "p|page_size=",
                    ("The number of rows to return per page. The server may return fewer rows " +
                     "than specified."),
                    (int p) => pageSize =  p
                },
                {
                    "f|filter=",
                    ("Query string to filter clients. If no filter is specified, all clients " +
                     "will be returned. By default, no filter will be set."),
                    f => filter = f
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
            parsedArgs["pageSize"] = pageSize ?? Utilities.MAX_PAGE_SIZE;
            parsedArgs["filter"] = filter;
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
            string parent = $"buyers/{accountId}";
            string pageToken = null;

            Console.WriteLine(@"Listing clients for buyer account ""{0}""", parent);
            do
            {
                BuyersResource.ClientsResource.ListRequest request =
                    mkService.Buyers.Clients.List(parent);
                request.Filter = (string) parsedArgs["filter"];
                request.PageSize = (int) parsedArgs["pageSize"];
                request.PageToken = pageToken;

                ListClientsResponse page = null;

                try
                {
                    page = request.Execute();
                }
                catch (Exception exception)
                {
                    throw new ApplicationException(
                        $"Marketplace API returned error response:\n{exception.Message}");
                }

                var clients = page.Clients;
                pageToken = page.NextPageToken;

                if (clients == null)
                {
                    Console.WriteLine("No clients found for buyer account.");
                }
                else
                {
                    foreach (Client client in clients)
                    {
                        Utilities.PrintClient(client);
                    }
                }
            }
            while(pageToken != null);
        }
    }
}
