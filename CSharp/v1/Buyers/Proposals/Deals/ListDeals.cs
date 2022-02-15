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

namespace Google.Apis.AuthorizedBuyersMarketplace.Examples.v1.Buyers.Proposals.Deals
{
    /// <summary>
    /// Lists deals for the given buyer's proposal.
    /// </summary>
    public class ListDeals : ExampleBase
    {
        private AuthorizedBuyersMarketplaceService mkService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public ListDeals()
        {
            mkService = Utilities.GetAuthorizedBuyersMarketplaceService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example lists deals for the given buyer's proposal.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {"account_id", "proposal_id"};
            bool showHelp = false;

            string accountId = null;
            string proposalId = null;
            int? pageSize = null;

            OptionSet options = new OptionSet {
                "List deals for a given buyer's proposal.",
                {
                    "h|help",
                    "Show help message and exit.",
                    h => showHelp = h != null
                },
                {
                    "a|account_id=",
                    ("[Required] The resource ID of the buyers resource under which the " +
                     "deals are being retrieved. This will be used to construct the parent used " +
                     "as a path parameter for the deals.list request."),
                    a => accountId = a
                },
                {
                    "proposal_id=",
                    ("[Required] The resource ID of the buyers.proposals resource under which " +
                     "the deals are being retrieved. This will be used to construct the parent " +
                     "used as a path parameter for the deals.list request."),
                    p => proposalId = p
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
            parsedArgs["proposal_id"] = proposalId;
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
            string proposalId = (string) parsedArgs["proposal_id"];
            string parent = $"buyers/{accountId}/proposals/{proposalId}";
            string pageToken = null;

            Console.WriteLine(@"Listing deals for proposal with name ""{0}""", parent);
            do
            {
                BuyersResource.ProposalsResource.DealsResource.ListRequest request =
                    mkService.Buyers.Proposals.Deals.List(parent);
                request.PageSize = (int) parsedArgs["pageSize"];
                request.PageToken = pageToken;

                ListDealsResponse page = null;

                try
                {
                    page = request.Execute();
                }
                catch (Exception exception)
                {
                    throw new ApplicationException(
                        $"Marketplace API returned error response:\n{exception.Message}");
                }

                var deals = page.Deals;
                pageToken = page.NextPageToken;

                if (deals == null)
                {
                    Console.WriteLine("No deals found for specified proposal.");
                }
                else
                {
                    foreach (Deal deal in deals)
                    {
                        Utilities.PrintDeal(deal);
                    }
                }
            }
            while(pageToken != null);
        }
    }
}
