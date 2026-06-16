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
# Cancels the ongoing negotiation for the specified proposal.
#
# This method is not supported for proposals including private auction deals
# because negotiation for that deal type can not be canceled. On successful
# cancelation, the proposal's state will be set to TERMINATED.
#
# This does not cancel or end serving for deals that have already been
# finalized. For finalized deals that are under renegotiation, calling this
# method will instead reset the proposal's state to FINALIZED.

require 'optparse'

require_relative '../../../util'


def cancel_negotiation_for_proposals(marketplace, options)
  name = "buyers/#{options[:account_id]}/proposals/#{options[:proposal_id]}"

  body = Google::Apis::AuthorizedbuyersmarketplaceV1::CancelNegotiationRequest.new()

  puts "Canceling negotiation for proposal with name '#{name}':"

  proposal = marketplace.cancel_proposal_negotiation(name, body)
  ProposalPrinter.print(proposal)
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
      'The resource ID of the buyers resource under which the proposal exists. This will be used to construct the '\
      'name used as a path parameter for the proposals.cancelNegotation request.',
      type: Integer, short_alias: 'a', required: true, default_value: nil
    ),
    Option.new(
      'proposal_id',
      'The resource ID of the buyers.proposals resource that is being canceled. This will be used to construct the '\
      'name used as a path parameter for the proposals.cancelNegotiation request.',
      short_alias: 'c', required: true, default_value: nil
    ),
  ]

  # Parse options.
  parser = Parser.new(options)
  opts = parser.parse(ARGV)

  begin
    cancel_negotiation_for_proposals(service, opts)
  rescue Google::Apis::ServerError => e
    raise "The following server error occured:\n#{e.message}"
  rescue Google::Apis::ClientError => e
    raise "Invalid client request:\n#{e.message}"
  rescue Google::Apis::AuthorizationError => e
    raise "Authorization error occured:\n#{e.message}"
  end
end
