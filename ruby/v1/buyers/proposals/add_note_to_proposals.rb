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
# Adds a note to a proposal for the given account and proposal IDs.
#
# This note will be visible to the seller and can be used to facilitate the
# negotiation process.

require 'optparse'

require_relative '../../../util'


def add_note_to_proposals(marketplace, options)
  name = "buyers/#{options[:account_id]}/proposals/#{options[:proposal_id]}"

  body = Google::Apis::AuthorizedbuyersmarketplaceV1::AddNoteRequest.new(
    note: Google::Apis::AuthorizedbuyersmarketplaceV1::Note.new(
      note: options[:note]
    )
  )

  puts "Adding note to proposal with name '#{name}':"

  proposal = marketplace.add_proposal_note(name, body)
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
      'The resource ID of the buyers resource under which the proposal was created. This will be used to construct '\
      'the name used as a path parameter for the proposals.addNote request.',
      type: Integer, short_alias: 'a', required: true, default_value: nil
    ),
    Option.new(
      'proposal_id',
      'The resource ID of the buyers.proposals resource that is having a note added to it. This will be used to '\
      'construct the name used as a path parameter for the proposals.addNote request.',
      short_alias: 'p', required: true, default_value: nil
    ),
    Option.new(
      'note',
      'The note to be added to the proposal.',
      short_alias: 'n', required: false, default_value: 'Created note from Ruby sample.'
    ),
  ]

  # Parse options.
  parser = Parser.new(options)
  opts = parser.parse(ARGV)

  begin
    add_note_to_proposals(service, opts)
  rescue Google::Apis::ServerError => e
    raise "The following server error occured:\n#{e.message}"
  rescue Google::Apis::ClientError => e
    raise "Invalid client request:\n#{e.message}"
  rescue Google::Apis::AuthorizationError => e
    raise "Authorization error occured:\n#{e.message}"
  end
end
