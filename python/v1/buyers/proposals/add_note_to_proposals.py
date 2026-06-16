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

"""Adds a note to a proposal for the given account and proposal IDs.

This note will be visible to the seller and can be used to facilitate the
negotiation process.
"""


import argparse
import os
import pprint
import sys

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
        'note': {
            'note': args.note
        }
    }

    print('Sending note to publisher for proposal with name '
          f'"{proposal_name}":')
    try:
        # Construct and execute the request.
        response = marketplace.buyers().proposals().addNote(
            proposal=proposal_name, body=body).execute()
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
        description='Send a note to the publisher for a specified proposal.')
    # Required fields.
    parser.add_argument(
        '-a', '--account_id', default=DEFAULT_BUYER_RESOURCE_ID,
        help=('The resource ID of the buyers resource under which the '
              'proposal was created. This will be used to construct the '
              'name used as a path parameter for the proposals.addNote '
              'request.'))
    parser.add_argument(
        '-p', '--proposal_id', default=DEFAULT_PROPOSAL_RESOURCE_ID,
        help=('The resource ID of the buyers.proposals resource for which the '
              'proposal was created. This will be used to construct the '
              'name used as a path parameter for the proposals.addNote '
              'request.'))
    # Optional fields.
    parser.add_argument(
        '-n', '--note', default='Created note from Python sample.',
        help='The note to be added to the proposal.')

    main(service, parser.parse_args())
