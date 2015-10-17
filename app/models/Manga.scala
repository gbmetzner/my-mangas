package models

import java.util.UUID

import org.joda.time.DateTime
import utils.Config
import utils.UUID._

/**
 * @author Gustavo Metzner on 10/13/15.
 */
case class Manga(id: UUID = generate(),
                 collection: String,
                 name: String,
                 number: String,
                 doIHaveIt: Boolean = false,
                 publicLink: String = Config.defaultMangaImage,
                 createdAt: DateTime = DateTime.now(),
                 updatedAt: DateTime = DateTime.now())