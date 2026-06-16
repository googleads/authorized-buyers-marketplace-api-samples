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
    /// Adds a creative to a given finalized deal that will be used in bids.
    ///
    /// It is recommended that those configuring programmatic guaranteed deals use this method to
    /// associate at least one creative that is ready to be placed in bids with the deal before
    /// signaling that the deal is ready to begin serving with finalizedDeals.setReadyToServe.
    ///
    /// A buyer's creatives can be viewed with the Real-time Bidding API:
    /// https://developers.google.com/authorized-buyers/apis/realtimebidding/reference/rest/v1/buyers.creatives
    /// </summary>
    public class AddCreativeToFinalizedDeals : ExampleBase
    {
        private AuthorizedBuyersMarketplaceService mkService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public AddCreativeToFinalizedDeals()
        {
            mkService = Utilities.GetAuthorizedBuyersMarketplaceService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example adds a creative to a given finalized deal.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {
                "account_id", "creative_id", "deal_id"};
            bool showHelp = false;

            string accountId = null;
            string creativeId = null;
            string dealId = null;

            OptionSet options = new OptionSet {
                "Adds a creative to the specified finalized deal for the given buyer account.",
                {
                    "h|help",
                    "Show help message and exit.",
                    h => showHelp = h != null
                },
                {
                    "a|account_id=",
                    ("[Required] The resource ID of the buyers resource under which the " +
                     "finalized deal is being accessed. This will be used to construct the " +
                     "finalized deal name used as a path parameter for the " +
                     "finalizedDeals.addCreative request, as well as the creative name included " +
                     "in the request body."),
                    a => accountId = a
                },
                {
                    "c|creative_id=",
                    ("[Required] The resource ID of the buyers.creatives resource that the " +
                     "buyer is adding to a finalized deal. This will be used to construct the " +
                     "creative name included in the body of the finalizedDeals.addCreative " +
                     "request."),
                    c => creativeId = c
                },
                {
                    "d|deal_id=",
                    ("[Required] The resource ID of the finalized deal that the creative is " +
                     "being added to. This will be used to construct the finalized deal name " +
                     "used as a path parameter for the finalizedDeals.addCreative request. "),
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
            parsedArgs["creative_id"] = creativeId;
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
            string creativeId = (string) parsedArgs["creative_id"];
            string dealId = (string) parsedArgs["deal_id"];
            string finalizedDealName = $"buyers/{accountId}/finalizedDeals/{dealId}";
            string creativeName = $"buyers/{accountId}/creatives/{creativeId}";

            AddCreativeRequest addCreativeRequest = new AddCreativeRequest()
            {
                Creative = creativeName
            };

            Console.WriteLine("Adding creative with name \"{0}\" to finalized deal with name " +
                "\"{1}\":", creativeName, finalizedDealName);

            BuyersResource.FinalizedDealsResource.AddCreativeRequest request =
                mkService.Buyers.FinalizedDeals.AddCreative(
                    addCreativeRequest, finalizedDealName);
            FinalizedDeal response = null;

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
