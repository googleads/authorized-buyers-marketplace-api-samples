#!/usr/bin/env ruby
# Encoding: utf-8
#
# Copyright:: Copyright 2022 Google LLC
#
# License:: Licensed under the Apache License, Version 2.0 (the "License");
#           you may not use this file except in compliance with the License.
#           You may obtain a copy of the License at
#
#           http://www.apache.org/licenses/LICENSE-2.0
#
#           Unless required by applicable law or agreed to in writing, software
#           distributed under the License is distributed on an "AS IS" BASIS,
#           WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
#           implied.
#           See the License for the specific language governing permissions and
#           limitations under the License.
#
# Patches a specified proposal at the given revision number.
#
# Fields that can be patched for this resource can be found in the reference
# documentation:
# https://developers.google.com/authorized-buyers/apis/marketplace/reference/rest/v1/buyers.proposals
#
# Note: If the revision number is lower than what is stored for the proposal
# server-side, the operation will be deemed obsolete and an error will be
# returned.
#
# Only proposals for preferred and programmatic guaranteed deals can be
# modified by buyers.

require 'optparse'
require 'securerandom'

require_relative '../../../util'


def patch_proposals(marketplace, options)
  name = "buyers/#{options[:account_id]}/proposals/#{options[:proposal_id]}"

  body = Google::Apis::AuthorizedbuyersmarketplaceV1::Proposal.new(
    proposal_revision: options[:proposal_revision],
    buyer_private_data: Google::Apis::AuthorizedbuyersmarketplaceV1::PrivateData.new(
      reference_id: "Marketplace-Ruby-Sample-Reference-#{SecureRandom.uuid}"
    )
  )

  puts "Patching proposal with name '#{name}':"

  patched_proposal = marketplace.patch_buyer_proposal(
      name, body, update_mask:'buyerPrivateData.referenceId')
  ProposalPrinter.print(patched_proposal)
end


if __FILE__ == $0
  begin
    # Retrieve the service used to make API requests.
    service = get_service()
  rescue ArgumentError => e
    raise 'Unable to create service, with error message: #{e.message}'
  rescue Signet::AuthorizationError => e
    raise 'Unable to create service, was the KEY_FILE in util.rb set? Error message: #{e.message}'
  end

  # Set options and default values for fields used in this example.
  options = [
    Option.new(
      'account_id',
      'The resource ID of the buyers resource under which the proposal was created. This will be used to construct '\
      'the name used as a path parameter for the proposals.patch request.',
      type: Integer, short_alias: 'a', required: true, default_value: nil
    ),
    Option.new(
      'proposal_id',
      'The resource ID of the buyers.proposals resource that is being patched. This will be used to construct the '\
      'name used as a path parameter for the proposals.patch request.',
      short_alias: 'p', required: true, default_value: nil
    ),
    Option.new(
      'proposal_revision',
      'The revision number for the proposal being modified. Each update to the proposal or its deals causes the '\
      'number to increment. The revision number specified must match the value stored server-side in order for the '\
      'operation to be performed.',
      short_alias: 'r', required: true, default_value: nil
    ),
  ]

  # Parse options.
  parser = Parser.new(options)
  opts = parser.parse(ARGV)

  begin
    patch_proposals(service, opts)
  rescue Google::Apis::ServerError => e
    raise "The following server error occured:\n#{e.message}"
  rescue Google::Apis::ClientError => e
    raise "Invalid client request:\n#{e.message}"
  rescue Google::Apis::AuthorizationError => e
    raise "Authorization error occured:\n#{e.message}"
  end
end
