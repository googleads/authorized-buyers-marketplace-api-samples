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

namespace Google.Apis.AuthorizedBuyersMarketplace.Examples.v1.Buyers.AuctionPackages
{
    /// <summary>
    /// Unsubscribes a given buyer account from a specified auction package.
    ///
    /// Once unsubscribed, the bidder will cease receiving bid requests for the auction package
    /// for the specified buyer.
    /// </summary>
    public class UnsubscribeFromAuctionPackages : ExampleBase
    {
        private AuthorizedBuyersMarketplaceService mkService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public UnsubscribeFromAuctionPackages()
        {
            mkService = Utilities.GetAuthorizedBuyersMarketplaceService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example unsubscribes a buyer account from an auction package.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {"account_id", "auction_package_id"};
            bool showHelp = false;

            string accountId = null;
            string auctionPackageId = null;

            OptionSet options = new OptionSet {
                "Unsubscribe the given buyer account from the specified auction package.",
                {
                    "h|help",
                    "Show help message and exit.",
                    h => showHelp = h != null
                },
                {
                    "a|account_id=",
                    ("[Required] The resource ID of the buyers resource that will be " +
                     "unsubscribing from an auction package. This will be used to construct the " +
                     "name used as a path parameter for the auctionPackages.unsubscribe request."),
                    a => accountId = a
                },
                {
                    "auction_package_id=",
                    ("[Required] The resource ID of the buyers.auctionPackages resource that " +
                     "the buyer is unsubscribing from. This will be used to construct the name  " +
                     "used as a path parameter for the auctionPackages.unsubscribe request."),
                    auction_package_id => auctionPackageId = auction_package_id
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
            parsedArgs["auction_package_id"] = auctionPackageId;
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
            string auctionPackageId = (string) parsedArgs["auction_package_id"];
            string name = $"buyers/{accountId}/auctionPackages/{auctionPackageId}";

            BuyersResource.AuctionPackagesResource.UnsubscribeRequest request =
                mkService.Buyers.AuctionPackages.Unsubscribe(new UnsubscribeAuctionPackageRequest(), name);
            AuctionPackage response = null;

            Console.WriteLine("Unsubscribing buyer \"{0}\" from auction package \"{1}\"",
                accountId, auctionPackageId);

            try
            {
                response = request.Execute();
            }
            catch (Exception exception)
            {
                throw new ApplicationException(
                    $"Marketplace API returned error response:\n{exception.Message}");
            }

            Utilities.PrintAuctionPackage(response);
        }
    }
}
