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

namespace Google.Apis.AuthorizedBuyersMarketplace.Examples.v1.Buyers.FinalizedDeals
{
    /// <summary>
    /// Lists finalized deals for a given buyer and their clients.
    /// </summary>
    public class ListFinalizedDeals : ExampleBase
    {
        private AuthorizedBuyersMarketplaceService mkService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public ListFinalizedDeals()
        {
            mkService = Utilities.GetAuthorizedBuyersMarketplaceService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example lists finalized deals for a given buyer and their clients.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {"account_id"};
            bool showHelp = false;

            string accountId = null;
            string filter = null;
            string orderBy = null;
            int? pageSize = null;

            string defaultFilter = "deal.dealType = PROGRAMMATIC_GUARANTEED";
            string defaultOrderBy = "deal.flightStartTime desc";

            OptionSet options = new OptionSet {
                "List finalized deals for the given buyer account and their clients.",
                {
                    "h|help",
                    "Show help message and exit.",
                    h => showHelp = h != null
                },
                {
                    "a|account_id=",
                    ("[Required] The resource ID of the buyers resource under which the " +
                     "finalized deals are being retrieved. This will be used to construct the " +
                     "parent used as a path parameter for the finalizedDeals.list request."),
                    a => accountId = a
                },
                {
                    "f|filter=",
                    ("Query string to filter finalized deals. By default, this example will " +
                     "fitler by deal type to retrieve programmatic guaranteed deals to " +
                     "demonstrate usage."),
                    f => filter =  f
                },
                {
                    "o|order_by=",
                    ("Query string used to sort the response of the list method. By default, " +
                     "this example will return deals in descending order of their flight start " +
                     "time to demonstrate usage. To learn more about the syntax for this " +
                     "parameter, see: " +
                     "https://cloud.google.com/apis/design/design_patterns#sorting_order"),
                    o => orderBy =  o
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
            parsedArgs["filter"] = filter ?? defaultFilter;
            parsedArgs["order_by"] = orderBy ?? defaultOrderBy;
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
            string parent = $"buyers/{accountId}";
            string pageToken = null;

            Console.WriteLine(@"Listing finalized deals for buyer account ""{0}""", parent);
            do
            {
                BuyersResource.FinalizedDealsResource.ListRequest request =
                    mkService.Buyers.FinalizedDeals.List(parent);
                request.Filter = (string) parsedArgs["filter"];
                request.OrderBy = (string) parsedArgs["order_by"];
                request.PageSize = (int) parsedArgs["pageSize"];
                request.PageToken = pageToken;

                ListFinalizedDealsResponse page = null;

                try
                {
                    page = request.Execute();
                }
                catch (Exception exception)
                {
                    throw new ApplicationException(
                        $"Marketplace API returned error response:\n{exception.Message}");
                }

                var finalizedDeals = page.FinalizedDeals;
                pageToken = page.NextPageToken;

                if (finalizedDeals == null)
                {
                    Console.WriteLine("No finalized deals found for buyer account.");
                }
                else
                {
                    foreach (FinalizedDeal finalizedDeal in finalizedDeals)
                    {
                        Utilities.PrintFinalizedDeal(finalizedDeal);
                    }
                }
            }
            while(pageToken != null);
        }
    }
}
