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

"""Patches a specified proposal at the given revision number.

Fields that can be patched for this resource can be found in the reference
documentation:
https://developers.google.com/authorized-buyers/apis/marketplace/reference/rest/v1/buyers.proposals

Note: if the revision number is lower than what is stored for the proposal
server-side, the operation will be deemed obsolete and an error will be
returned.

Only proposals for preferred and programmatic guaranteed deals can be modified
by buyers.
"""


import argparse
import os
import pprint
import sys
import uuid

sys.path.insert(0, os.path.abspath('../../..'))

from googleapiclient.errors import HttpError

import util


_PROPOSALS_NAME_TEMPLATE = 'buyers/%s/proposals/%s'

DEFAULT_BUYER_RESOURCE_ID = 'ENTER_BUYER_RESOURCE_ID_HERE'
DEFAULT_PROPOSAL_RESOURCE_ID = 'ENTER_PROPOSAL_RESOURCE_ID_HERE'


def main(marketplace, args):
    proposal_name = _PROPOSALS_NAME_TEMPLATE % (
        args.account_id, args.proposal_id)

    body = {
        'proposalRevision': args.proposal_revision,
        'buyerPrivateData': {
            'referenceId': f'Marketplace-Python-Sample-Reference-{uuid.uuid4()}'
        }
    }

    update_mask = 'buyerPrivateData.referenceId'

    print(f'Patching proposal with name "{proposal_name}":')
    try:
        # Construct and execute the request.
        response = marketplace.buyers().proposals().patch(
            name=proposal_name, body=body, updateMask=update_mask).execute()
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
        description='Patch a specified proposal at the given revision number.')
    # Required fields.
    parser.add_argument(
        '-a', '--account_id', default=DEFAULT_BUYER_RESOURCE_ID,
        help=('The resource ID of the buyers resource under which the proposal '
              'was created. This will be used to construct the name used as a '
              'path parameter for the proposals.patch request.'))
    parser.add_argument(
        '-p', '--proposal_id', default=DEFAULT_PROPOSAL_RESOURCE_ID,
        help=('The resource ID of the buyers.proposals resource for which the '
              'proposal was created. This will be used to construct the name '
              'used as a path parameter for the proposals.patch request.'))
    parser.add_argument(
        '-r', '--proposal_revision', required=True,
        help=('The revision number for the proposal being modified. Each '
              'update to the proposal or its deals causes the number to '
              'increment. The revision number specified must match the value '
              'stored server-side in order for the operation to be performed.'))

    main(service, parser.parse_args())
