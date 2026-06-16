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
use Google\Service\AuthorizedBuyersMarketplace\AddNoteRequest;
use Google\Service\AuthorizedBuyersMarketplace\Note;

/**
 * This example illustrates how to add a note for the given buyer account and proposal IDs.
 *
 * This note will be visible to the seller and can be used to facilitate the negotiation process.
 */
class AddNoteToProposals extends BaseExample
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
                    'for the proposals.addNote request.',
                'required' => true
            ],
            [
                'name' => 'proposal_id',
                'display' => 'Proposal ID',
                'description' =>
                    'The resource ID of the buyers.proposals resource for which the proposal ' .
                    'was created. This will be used to construct the name used as a path ' .
                    'parameter for the proposals.addNote request.',
                'required' => true
            ],
            [
                'name' => 'note',
                'display' => 'Note',
                'description' => 'The note to be added to the proposal.',
                'required' => false,
                'default' => 'Created note from PHP sample.'
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

        $addNoteRequest = new AddNoteRequest();
        $addNoteRequest->note = Note();
        $addNoteRequest->note->note = $values['note'];

        print "<h2>Adding note to proposal with name '$name':</h2>";
        $result = $this->service->buyers_proposals->addNote($name, $addNoteRequest);
        $this->printResult($result);
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'Add Note to Proposal';
    }
}
