<?php

/**
 * Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

namespace Google\Ads\AuthorizedBuyers\Marketplace\Examples\V1\Buyers_Proposals;

use Google\Ads\AuthorizedBuyers\Marketplace\ExampleUtil\BaseExample;
use Google\Ads\AuthorizedBuyers\Marketplace\ExampleUtil\Config;
use Google\Service\AuthorizedBuyersMarketplace\AcceptProposalRequest;
use Google\Service\Exception as GoogleServiceException;

/**
 * This example illustrates how to accept a proposal for the given buyer account and proposal IDs.
 *
 * Note that a proposal can only be accepted if it is in the BUYER_ACCEPTANCE_REQUESTED state. Once
 * both a buyer and seller have accepted a proposal, its state will change to FINALIZED.
 *
 * If the revision number specified is lower than the latest stored by the API, an error message
 * will be returned.
 */
class AcceptProposals extends BaseExample
{
    public function __construct($client)
    {
        $this->service = Config::getGoogleServiceAuthorizedBuyersMarketplace($client);
    }

    /**
     * @see BaseExample::getInputParameters()
     */
    protected function getInputParameters()
    {
        return [
            [
                'name' => 'account_id',
                'display' => 'Account ID',
                'description' =>
                    'The resource ID of the buyers resource under which the proposal was ' .
                    'created. This will be used to construct the name used as a path parameter ' .
                    'for the proposals.accept request.',
                'required' => true
            ],
            [
                'name' => 'proposal_id',
                'display' => 'Proposal ID',
                'description' =>
                    'The resource ID of the buyers.proposals resource for which the proposal ' .
                    'was created. This will be used to construct the name used as a path ' .
                    'parameter for the proposals.accept request.',
                'required' => true
            ]
        ];
    }

    /**
     * @see BaseExample::run()
     */
    public function run()
    {
        $values = $this->formValues;

        $name = "buyers/$values[account_id]/proposals/$values[proposal_id]";

        $acceptProposalRequest = new AcceptProposalRequest();

        print "<h2>Accepting proposal with name: '$name':</h2>";

        try {
            $result = $this->service->buyers_proposals->accept($name, $acceptProposalRequest);
            $this->printResult($result);
        } catch (GoogleServiceException $ex) {
            print "<h3>Accepting proposal was unsuccessful.</h3>";
            $errorResponse = $ex->getMessage();
            print "<p>Error response:</br><code>$errorResponse</code></p>";
        }
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'Accept Proposal';
    }
}
