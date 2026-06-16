#!/usr/bin/python
#
# Copyright 2021 Google Inc. All Rights Reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

"""Patch the user list targeting of one or more deals for the given proposal.

This operation requires that the deals all exist under the same proposal.

The user list targeting of the given deals will be modified to target the
specified user lists. User lists can be retrieved via the Real-time Bidding
API's buyers.userLists resource. You can learn more about buyers.userLists in
the reference documentation:
https://developers.google.com/authorized-buyers/apis/realtimebidding/reference/rest/v1/buyers.userLists

Note: Only preferred and programmatic guaranteed deals an be modified by the
buyer; attempting to modify a private auction deal will result in an error
response.
"""


import argparse
import os
import pprint
import sys

sys.path.insert(0, os.path.abspath('../../../..'))

from googleapiclient.errors import HttpError

import util


_PROPOSALS_NAME_TEMPLATE = 'buyers/%s/proposals/%s'
_DEALS_NAME_TEMPLATE = 'buyers/%s/proposals/%s/deals/%s'


DEFAULT_BUYER_RESOURCE_ID = 'ENTER_BUYER_RESOURCE_ID_HERE'
DEFAULT_PROPOSAL_RESOURCE_ID = 'ENTER_PROPOSAL_RESOURCE_ID_HERE'


def main(marketplace, args):
    account_id = args.account_id
    proposal_id = args.proposal_id
    proposal_name = _PROPOSALS_NAME_TEMPLATE % (account_id, proposal_id)

    # This will create a update deal request for each given deal for the
    # specified proposal revision. Each request will patch userListTargeting
    # for the deal such that the given user list IDs are targeted.
    body = {
        'requests': [{
            'deal': {
                'name': _DEALS_NAME_TEMPLATE % (
                    account_id, proposal_id, deal_id),
                'proposalRevision': args.proposal_revision,
                'targeting': {
                    'userListTargeting': {
                        'targetedCriteriaIds': [
                            user_list_id for user_list_id in args.user_list_ids]
                    }
                },
            },
            'updateMask': 'targeting.userListTargeting.targetedCriteriaIds'
        } for deal_id in args.deal_ids],
    }

    print(f'Batch updating deals for proposal "{proposal_name}":')
    try:
        # Construct and execute the request.
        response = marketplace.buyers().proposals().deals().batchUpdate(
            parent=proposal_name, body=body).execute()
    except HttpError as e:
        print(e)
        sys.exit(1)

    pprint.pprint(response)


if __name__ == '__main__':
    try:
        service = util.get_service(version='v1')
    except IOError as ex:
        print(f'Unable to create marketplace service - {ex}')
        print('Did you specify the key file in util.py?')
        sys.exit(1)

    parser = argparse.ArgumentParser(
        description=('Batch update user list targeting for multiple deals '
                     'associated with a given buyer\'s proposal.'))
    # Required fields.
    parser.add_argument(
        '-a', '--account_id', default=DEFAULT_BUYER_RESOURCE_ID,
        help=('The resource ID of the buyers resource under which the '
              'proposal was created. This will be used to construct the '
              'name used as a path parameter for the deals.batchUpdate '
              'request.'))
    parser.add_argument(
        '-p', '--proposal_id', default=DEFAULT_PROPOSAL_RESOURCE_ID,
        help=('The resource ID of the buyers.proposals resource under which '
              'one or more deals were created. This will be used to construct '
              'the name used as a path parameter for the deals.batchUpdate '
              'request.'))
    parser.add_argument(
        '-d', '--deal_ids', required=True, nargs='+',
        help=('One or more resource IDs for the buyers.proposals.deals '
              'resource that will be patched in a batch update operation. This '
              'will be used to construct the deal name that is included in the '
              'update request for each deal in the deals.batchUpdate request.'))
    parser.add_argument(
        '-r', '--proposal_revision', required=True,
        help=('The revision number for the proposal associated with the deals '
              'being modified. Each update to the proposal or its deals causes '
              'the number to increment. The revision number specified must '
              'match the value stored server-side in order for the operation '
              'to be performed.'))
    parser.add_argument(
        '-u', '--user_list_ids', nargs='+',
        help=('One or more resource IDs for the buyers.userLists resources '
              'that are to be targeted by the given deals. Specify each user '
              'list ID separated by a space.'))

    main(service, parser.parse_args())
