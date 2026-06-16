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
    /// Gets a single finalized deal for the given buyer and deal IDs.
    ///
    /// Note that deal IDs for finalized deals are identical to those of
    /// non-finalized deals–when a deal becomes finalized, its deal ID will not
    /// change.
    /// </summary>
    public class GetFinalizedDeals : ExampleBase
    {
        private AuthorizedBuyersMarketplaceService mkService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public GetFinalizedDeals()
        {
            mkService = Utilities.GetAuthorizedBuyersMarketplaceService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example gets a finalized deal for given buyer and deal IDs.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {"account_id", "deal_id"};
            bool showHelp = false;

            string accountId = null;
            string dealId = null;

            OptionSet options = new OptionSet {
                "Get a finalized deal for the given buyer and deal IDs.",
                {
                    "h|help",
                    "Show help message and exit.",
                    h => showHelp = h != null
                },
                {
                    "a|account_id=",
                    ("[Required] The resource ID of the buyers resource under which the auction " +
                     "package is being retrieved. This will be used to construct the name used " +
                     "as a path parameter for the auctionPackages.get request."),
                    a => accountId = a
                },
                {
                    "d|deal_id=",
                    ("[Required] The resource ID of the buyers.finalizedDeals resource that " +
                     "is being retrieved. This will be used to construct the name used as a " +
                     "path parameter for the finalizedDeals.get request."),
                    d => dealId = d
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
            parsedArgs["deal_id"] = dealId;
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
            string dealId = (string) parsedArgs["deal_id"];
            string name = $"buyers/{accountId}/finalizedDeals/{dealId}";

            BuyersResource.FinalizedDealsResource.GetRequest request =
                mkService.Buyers.FinalizedDeals.Get(name);
            FinalizedDeal response = null;

            Console.WriteLine("Getting finalized deal with name: {0}", name);

            try
            {
                response = request.Execute();
            }
            catch (Exception exception)
            {
                throw new ApplicationException(
                    $"Marketplace API returned error response:\n{exception.Message}");
            }

            Utilities.PrintFinalizedDeal(response);
        }
    }
}
