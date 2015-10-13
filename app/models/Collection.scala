package models

import java.util.UUID

import org.joda.time.DateTime
import utils.UUID._

/**
 * @author Gustavo Metzner on 10/13/15.
 */
case class Collection(id: UUID = generate(),
                      name: String,
                      createdAt: DateTime = DateTime.now(),
                      updatedAt: DateTime = DateTime.now()) {

}
