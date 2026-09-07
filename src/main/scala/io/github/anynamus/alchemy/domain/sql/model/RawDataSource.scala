package io.github.anynamus.alchemy.domain.sql.model

final case class RawDataSource(
                                table: String,
                                input: String
                              )