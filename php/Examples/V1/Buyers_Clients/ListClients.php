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

namespace Google\Ads\AuthorizedBuyers\Marketplace\Examples\V1\Buyers_Clients;

use Google\Ads\AuthorizedBuyers\Marketplace\ExampleUtil\BaseExample;
use Google\Ads\AuthorizedBuyers\Marketplace\ExampleUtil\Config;

/**
 * This example illustrates how to list clients for the given buyer's account ID.
 */
class ListClients extends BaseExample
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
                'display' => 'Buyer account ID',
                'required' => true,
                'description' =>
                    'The resource ID of the buyers resource under which the clients were ' .
                    'created. This will be used to construct the parent used as a path ' .
                    'parameter for the clients.list request.'
            ],
            [
                'name' => 'filter',
                'display' => 'Filter',
                'required' => false,
                'description' =>
                    'Query string to filter clients. If no filter is specified, all clients ' .
                    'be returned. By default, no filter will be set.',
                'default' => null
            ],
            [
                'name' => 'page_size',
                'display' => 'Page size',
                'required' => false,
                'description' =>
                    'The number of rows to return per page. The server may return fewer rows ' .
                    'than specified. By default, a page size of 10 will be set.',
                'default' => 10
            ]
        ];
    }

    /**
     * @see BaseExample::run()
     */
    public function run()
    {
        $values = $this->formValues;

        $parentName = "buyers/$values[account_id]";

        $queryParams = [
            'filter' => $values['filter'],
            'pageSize' => $values['page_size'],
        ];

        $result = $this->service->buyers_clients->listBuyersClients($parentName, $queryParams);

        print "<h2>Clients found for '$parentName':</h2>";
        if (empty($result['clients'])) {
            print '<p>No Clients found</p>';
        } else {
            foreach ($result['clients'] as $client) {
                $this->printResult($client);
            }
        }
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'List Clients';
    }
}
