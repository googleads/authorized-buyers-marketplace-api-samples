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

namespace Google.Apis.AuthorizedBuyersMarketplace.Examples.v1.Buyers.PublisherProfiles
{
    /// <summary>
    /// Gets a single publisher profile for the given buyer and publisher profile ID.
    /// </summary>
    public class GetPublisherProfiles : ExampleBase
    {
        private AuthorizedBuyersMarketplaceService mkService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public GetPublisherProfiles()
        {
            mkService = Utilities.GetAuthorizedBuyersMarketplaceService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example gets a specific publisher profile for a buyer account.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {"account_id", "publisher_profile_id"};
            bool showHelp = false;

            string accountId = null;
            string publisherProfileId = null;

            OptionSet options = new OptionSet {
                "Get a publisher profile for a given buyer and publisher profile ID.",
                {
                    "h|help",
                    "Show help message and exit.",
                    h => showHelp = h != null
                },
                {
                    "a|account_id=",
                    ("[Required] The resource ID of the buyers resource under which the " +
                     "publisherProfiles resource is being accessed. This will be used to " +
                     "construct the name used as a path parameter for the publisherProfiles.get " +
                     "request."),
                    a => accountId = a
                },
                {
                    "p|publisher_profile_id=",
                    ("[Required] The resource ID of the buyers.publisherProfiles resource that " +
                     "is being accessed. This will be used to construct the name used as a " +
                     "path parameter for the publisherProfiles.get request."),
                    p => publisherProfileId = p
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
            parsedArgs["publisher_profile_id"] = publisherProfileId;
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
            string publisherProfileId = (string) parsedArgs["publisher_profile_id"];
            string name = $"buyers/{accountId}/publisherProfiles/{publisherProfileId}";

            BuyersResource.PublisherProfilesResource.GetRequest request =
                mkService.Buyers.PublisherProfiles.Get(name);
            PublisherProfile response = null;

            Console.WriteLine("Getting publisher profile with name: {0}", name);

            try
            {
                response = request.Execute();
            }
            catch (Exception exception)
            {
                throw new ApplicationException(
                    $"Marketplace API returned error response:\n{exception.Message}");
            }

            Utilities.PrintPublisherProfile(response);
        }
    }
}
