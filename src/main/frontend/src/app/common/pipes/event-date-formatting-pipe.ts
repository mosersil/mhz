import {Inject, LOCALE_ID, Pipe, PipeTransform} from "@angular/core";
import {Event} from "../entities/event";
import {formatDate} from "@angular/common";



@Pipe({ name: 'eventDateFormattingPipe' })
export class EventDateFormattingPipe implements PipeTransform{

  constructor(@Inject(LOCALE_ID) private locale: string) {}

  transform(event: Event, format = 'mediumDate', timezone?: string, locale?: string): string|null {

    let dateStart:Date =  new Date(event.dateStart);
    let dateEnd = new Date(event.dateEnd);

    var isSameDay = (dateStart.getDate() === dateEnd.getDate()
      && dateStart.getMonth() === dateEnd.getMonth()
      && dateStart.getFullYear() === dateEnd.getFullYear())

    if (isSameDay) {
      return formatDate(dateStart, format, locale || this.locale, timezone);
    } else {
      return formatDate(dateStart, format, locale || this.locale, timezone) + " - " + formatDate(dateEnd, format, locale || this.locale, timezone)
    }

  }




}
